import java.util.*;

public class Automaton {
    private String name;
    private List<String> states;
    private List<String> alphabet;
    private String initialState;
    private List<String> finalStates;
    private List<Transition> transitions;
    
    public Automaton(String name, List<String> states, List<String> alphabet,
                     String initialState, List<String> finalStates,
                     List<Transition> transitions) {
        this.name = name;
        this.states = states;
        this.alphabet = alphabet;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Autômato: ").append(name).append(" ===\n");
        sb.append("Estados: ").append(states).append("\n");
        sb.append("Alfabeto: ").append(alphabet).append("\n");
        sb.append("Estado Inicial: ").append(initialState).append("\n");
        sb.append("Estados Finais: ").append(finalStates).append("\n");
        sb.append("Transições:\n");
        for (Transition t : transitions) {
            sb.append("  ").append(t).append("\n");
        }
        return sb.toString();
    }
    
    // Getters
    public String getName() { return name; }
    public List<String> getStates() { return states; }
    public List<String> getAlphabet() { return alphabet; }
    public String getInitialState() { return initialState; }
    public List<String> getFinalStates() { return finalStates; }
    public List<Transition> getTransitions() { return transitions; }
}

class Transition {
    private String fromState;
    private String symbol;
    private String toState;
    
    public Transition(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
    
    @Override
    public String toString() {
        return "δ(" + fromState + ", " + symbol + ") = " + toState;
    }
    
    // Getters
    public String getFromState() { return fromState; }
    public String getSymbol() { return symbol; }
    public String getToState() { return toState; }
}
