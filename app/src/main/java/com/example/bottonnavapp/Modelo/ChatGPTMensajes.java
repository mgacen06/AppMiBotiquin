package com.example.bottonnavapp.Modelo;

public class ChatGPTMensajes {

    private String IdUsuario;

    //Pongo un ID de receptor por si en un futuro
    //quiero incorporar mensajes con profesionales
    //Quede registrado quien envia cada cosa y para
    // que diferencie entre mensajes de el usuario a
    // chatGPT y los que son de chatGPT a el usuario
    private String IdReceptor;
    private String Mensaje;
    private long TimeStamp;

    public ChatGPTMensajes() {
    }

    public ChatGPTMensajes(String idUsuario, String idReceptor, String mensaje, long timeStamp) {
        IdUsuario = idUsuario;
        IdReceptor = idReceptor;
        Mensaje = mensaje;
        TimeStamp = timeStamp;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getIdReceptor() {
        return IdReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        IdReceptor = idReceptor;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
