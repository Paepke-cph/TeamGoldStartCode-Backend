package entity;

public class CombinedJoke {
    private String joke1, joke1Reference, joke2, joke2Reference;

    public CombinedJoke(ChuckDTO chuckDTO, DadDTO dadDTO) {
        this.joke1 = chuckDTO.getValue();
        this.joke1Reference = chuckDTO.getUrl();
        this.joke2 = dadDTO.joke;
        this.joke2Reference = dadDTO.getUrl();
    }
}
