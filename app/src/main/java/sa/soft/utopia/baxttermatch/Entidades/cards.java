package sa.soft.utopia.baxttermatch.Entidades;

public class cards {
    private String userId;
    private String nombre;

    public cards(String userId, String nombre) {
        this.userId = userId;
        this.nombre = nombre;
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
}
