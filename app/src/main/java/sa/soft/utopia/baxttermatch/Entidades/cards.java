package sa.soft.utopia.baxttermatch.Entidades;

public class cards {
    private String userId;
    private String nombre;
    private String imgPerfil;

    public cards(String userId, String nombre, String imgPerfil) {
        this.userId = userId;
        this.nombre = nombre;
        this.imgPerfil = imgPerfil;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }
}
