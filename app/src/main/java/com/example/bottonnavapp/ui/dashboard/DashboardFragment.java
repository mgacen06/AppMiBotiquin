package com.example.bottonnavapp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottonnavapp.API.OpenAIApi;
import com.example.bottonnavapp.API.OpenAIRequest;
import com.example.bottonnavapp.API.OpenAIResponse;
import com.example.bottonnavapp.API.RetrofitClient;
import com.example.bottonnavapp.Modelo.ChatGPTMensajes;
import com.example.bottonnavapp.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    // Nuevas variables para chatGPT
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ProgressBar progressBar;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatGPTMensajes> chatMessages;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar FirebaseAuth y FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        chatRecyclerView = binding.chatRecyclerView;
        messageEditText = binding.messageEditText;
        sendButton = binding.sendButton;
        progressBar = binding.progressBar;


        chatMessages = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(chatMessages);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatMessageAdapter);

        // Configurar el botón de enviar
        sendButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString();
            if (!messageText.isEmpty()) {
                // Mostrar ProgressBar cuando se envía el mensaje
                progressBar.setVisibility(View.VISIBLE);
                sendMessage(messageText);
                messageEditText.setText("");

                // Ocultar el teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        // Escuchar mensajes en Firestore
        listenForMessages();
        return root;
    }

    // Enviar mensaje a Firestore y llamar a la API de OpenAI
    private void sendMessage(String messageText) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String senderId = currentUser.getUid();
            String receiverId = "AI";
            long timestamp = System.currentTimeMillis();

            ChatGPTMensajes chatMessage = new ChatGPTMensajes(senderId, receiverId, messageText, timestamp);

            db.collection("chats").add(chatMessage)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("DashboardFragment", "Message sent successfully: " + messageText);
                        // Mensaje enviado exitosamente
                        callOpenAI(messageText);

                        //Mando el mensaje a recicleview
                        List<ChatGPTMensajes> messages = new ArrayList<>();
                            messages.add(chatMessage);
                            Log.d("DashboardFragment", "Received message: " + chatMessage.getMensaje());
                        chatMessageAdapter.updateMessages(messages);
                        chatRecyclerView.scrollToPosition(chatMessageAdapter.getItemCount() - 1);

                    })
                    .addOnFailureListener(e -> {
                        Log.w("DashboardFragment", "Error sending message", e);
                        // Error al enviar mensaje
                        //Quitamos la visibilidad de la progressBar
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }

    // Llamar a la API de OpenAI
    private void callOpenAI(String userMessage) {
        OpenAIApi apiService = RetrofitClient.getClient("https://api.openai.com/").create(OpenAIApi.class);

        //Pongo al asistente en un contexto concreto en cada mensaje, pero ese contexto no se guarda en firebase
        String contextPrefix = "Eres una asistente de salud, recomienda medicamentos a tomar o pequeños consejos de salud y bienestar intenta siempre recomendar un medicamento a tomar antes de responderle que no puedes decirle nada, si te hacen preguntas que no sean sobre salud tienes que decir que lo sientes, que eres un asistente en temas de salud. Proporciona respuestas relacionadas con salud y medicina, no des respuestas muy largas, hazla un parrafo mas o menos. Se cuidadoso con tus respuestas y si un tema es complejo dile al usuario que consulte con un profesional, para responder sobre algun medicamento busca en internet los nombres de medicamentos en la actualidad en españa, y ten en cuenta también la lista que proporciona CIMA en https://cima.aemps.es/cima/publico/home.html o a traves de su API en https://www.aemps.gob.es/apps/cima/docs/CIMA_REST_API.pdf. Quiero que todo lo que he dicho ahora lo tengas en cuenta pero no respondas a ello, solo tienes que responder a la consulta que vas a ver despues de estos dos puntos:  ";
        String completeMessage = contextPrefix + userMessage;

        List<OpenAIRequest.Message> messages = new ArrayList<>();
        messages.add(new OpenAIRequest.Message("user", completeMessage));

        OpenAIRequest request = new OpenAIRequest("gpt-3.5-turbo", messages);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Call<OpenAIResponse> call = apiService.getChatResponse(request);
            call.enqueue(new Callback<OpenAIResponse>() {
                @Override
                public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        String botMessage = response.body().getChoices().get(0).getMessage().getContent();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String senderId = "AI";
                            String receiverId = currentUser.getUid();
                            long timestamp = System.currentTimeMillis();

                            ChatGPTMensajes chatMessage = new ChatGPTMensajes(senderId, receiverId, botMessage, timestamp);

                            db.collection("chats").add(chatMessage)
                                    .addOnSuccessListener(documentReference -> {
                                        // Mensaje a la IA enviado exitosamente
                                        Log.d("DashboardFragment", "Bot message sent successfully: " + botMessage);

                                        //Mando la respuesta a recicleview
                                        List<ChatGPTMensajes> messages = new ArrayList<>();
                                        messages.add(chatMessage);
                                        Log.d("DashboardFragment", "Received message: " + botMessage);
                                        chatMessageAdapter.updateMessages(messages);
                                        chatRecyclerView.scrollToPosition(chatMessageAdapter.getItemCount() - 1);


                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al enviar el mensaje a la IA
                                        Log.w("DashboardFragment", "Error sending bot message", e);

                                    });
                        }
                    } else {
                        if (response.code() == 429) {
                            Log.e("APIError", "Excedido el límite de cuota: " + response.message());
                        } else {
                            Log.e("APIError", "Error en la respuesta de la API: " + response.message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                    // Error en la llamada a la API
                    progressBar.setVisibility(View.GONE);
                }
            });
            //Añado 1 segundo de retraso entre consultas para que no sature a la API
        }, 1000);
    }

    // Escuchar cambios en Firestore
    private void listenForMessages() {
        db.collection("chats")
                .orderBy("timestamp")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w("DashboardFragment", "Listen failed.", e);
                        return;
                    } else {
                        //Me esta llegando aquí, ha leido bien
                        Log.w("DashboardFragment", "Listen successful.", e);
                    }
                    if (queryDocumentSnapshots != null) {
                        Log.d("DashboardFragment", "QueryDocumentSnapshots != null" + queryDocumentSnapshots.toString());
                        List<ChatGPTMensajes> messages = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("DashboardFragment", "Document data: " + document.getData());
                            ChatGPTMensajes message = document.toObject(ChatGPTMensajes.class);
                            messages.add(message);
                            Log.d("DashboardFragment", "Received message: " + message.getMensaje());
                        }

                        chatMessageAdapter.updateMessages(messages);
                        Log.d("DashboardFragment", "Total messages: " + messages.size());

                    } else {
                        Log.d("DashboardFragment", "No messages found");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}