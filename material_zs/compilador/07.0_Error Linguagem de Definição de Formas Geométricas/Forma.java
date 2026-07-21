import java.util.*;

// Classe para representar uma forma geométrica
class Forma {
    String tipo;
    String nome;
    List<Propriedade> propriedades;
    int linha, coluna;
    
    public Forma(String tipo, String nome, List<Propriedade> propriedades, 
                 int linha, int coluna) {
        this.tipo = tipo;
        this.nome = nome;
        this.propriedades = propriedades;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    private boolean temPropriedade(String nome) {
        for (Propriedade p : propriedades) {
            if (p.nome.equals(nome)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean validarCirculo() {
        return temPropriedade("raio");
    }
    
    public boolean validarRetangulo() {
        return temPropriedade("largura") && temPropriedade("altura");
    }
    
    public boolean validarTriangulo() {
        return temPropriedade("base") && temPropriedade("altura");
    }
    
    public boolean validarQuadrado() {
        return temPropriedade("lado");
    }
    
    public boolean validarElipse() {
        return temPropriedade("largura") && temPropriedade("altura");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(tipo.toUpperCase()).append(" '").append(nome).append("'");
        sb.append(" (linha ").append(linha).append(", coluna ").append(coluna).append("):");
        
        for (Propriedade p : propriedades) {
            if (!p.nome.equals("erro")) {
                sb.append("\n  - ").append(p);
            }
        }
        
        return sb.toString();
    }
}
