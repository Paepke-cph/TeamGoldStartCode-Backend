package dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name ="Joke")
public class CombinedJoke {
    @Schema(required = true, example = "....random Chuck Noris joke")
    public String joke1;
    @Schema(required = true, example = "https://api.chucknorris.io/jokes/random")
    public String joke1Reference;
    @Schema(required = true, example = ".....random dad joke")
    public String joke2;
    @Schema(required = true, example = "https://icanhazdadjoke.com")
    public String joke2Reference;

    public CombinedJoke(ChuckDTO chuckDTO, DadDTO dadDTO) {
        this.joke1 = chuckDTO.getValue();
        this.joke1Reference = chuckDTO.getUrl();
        this.joke2 = dadDTO.joke;
        this.joke2Reference = dadDTO.getUrl();
    }
}
