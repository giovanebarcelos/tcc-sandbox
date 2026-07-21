// Classe para representar uma posição (x, y)
class Posicao {
    double x, y;
    
    public Posicao(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
