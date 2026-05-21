package bloco3;

/**
 * Classe Usuario para ser usada em GerenciadorSessao
 */
public class Usuario {
    private long id;
    private String perfil;

    public Usuario(long id, String perfil) {
        this.id = id;
        this.perfil = perfil;
    }

    public long getId() {
        return id;
    }

    public String getPerfil() {
        return perfil;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", perfil='" + perfil + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
