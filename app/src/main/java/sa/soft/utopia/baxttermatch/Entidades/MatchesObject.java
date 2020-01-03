package sa.soft.utopia.baxttermatch.Entidades;

public class MatchesObject {
    private String userId;
    private String nombre;
    private String imagenPerfil;

    public MatchesObject(String userId, String nombre, String imagenPerfil) {
        this.userId = userId;
        this.nombre = nombre;
        this.imagenPerfil = imagenPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
