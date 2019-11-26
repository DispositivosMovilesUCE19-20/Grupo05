package ec.edu.uce.optativa3.modelo;

public class Estudiante {

    private String id ;
    private String password;
    private String nombre;
    private String apellido;
    private String correo;
    private String celular;
    private String genero;
    private String materias;
    private String fechaNacimiento;
    private String beca;

    public Estudiante() {

    }

    /*public Estudiante(String id, String password, String nombre, String apellido, String correo, String celular, String genero, String materias, String fechaNacimiento, String beca) {
        this.id = id;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.celular = celular;
        this.genero = genero;
        this.materias = materias;
        this.fechaNacimiento = fechaNacimiento;
        this.beca = beca;
    }*/
    public Estudiante(String apellido) {
        this.apellido = apellido;
        //this.celular = celular;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getMaterias() {
        return materias;
    }

    public void setMaterias(String materias) {
        this.materias = materias;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getBeca() {
        return beca;
    }

    public void setBeca(String beca) {
        this.beca = beca;
    }
}
