package com.example.bottonnavapp.Modelo;

public class ChatGPTMensajes {

    private String idUsuario;

    //Pongo un ID de receptor por si en un futuro
    //quiero incorporar mensajes con profesionales
    //Quede registrado quien envia cada cosa y para
    // que diferencie entre mensajes de el usuario a
    // chatGPT y los que son de chatGPT a el usuario
    private String idReceptor;
    private String mensaje;
    private long timeStamp;

    public ChatGPTMensajes() {
    }

    public ChatGPTMensajes(String idUsuario, String idReceptor, String mensaje, long timeStamp) {
        this.idUsuario = idUsuario;
        this.idReceptor = idReceptor;
        this.mensaje = mensaje;
        this.timeStamp = timeStamp;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
