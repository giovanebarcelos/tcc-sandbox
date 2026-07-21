// Classe para representar uma propriedade
class Propriedade {
    String nome;
    Object valor;
    int linha, coluna;
    
    public Propriedade(String nome, Object valor, int linha, int coluna) {
        this.nome = nome;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    @Override
    public String toString() {
        return nome + ": " + valor;
    }
}
