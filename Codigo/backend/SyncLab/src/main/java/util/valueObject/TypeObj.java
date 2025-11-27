package util.valueObject;

public class TypeObj {
    String value;

    public TypeObj(String value) {
        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public static TypeObj getInformatica() {
        return new TypeObj("Informatica");
    } 
    public static TypeObj getMonitoria() {
        return new TypeObj("Monitoria");
    } 
    public static TypeObj getLivre() {
        return new TypeObj("Livre");
    } 
    public static TypeObj getQuimica() {
        return new TypeObj("Quimica");
    } 
    public static TypeObj getOficina() {
        return new TypeObj("Oficina");
    } 
}