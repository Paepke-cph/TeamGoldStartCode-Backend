package dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import webscraper.TagCounter;

@Schema(name = "Tag")
public class TagDTO {

  public TagDTO(TagCounter tc) {
    this.url = tc.getUrl();
    this.title = tc.getTitle();
    this.divCount = tc.getDivCount();
    this.bodyCount = tc.getBodyCount();
  }
  @Schema(required = true, example = "www.google.com")
  public String url;
  @Schema(required = true, example = "Google")
  public String title;
  @Schema(required = true, example = "153")
  public int divCount;
  @Schema(required = true, example = "1")
  public int bodyCount;
}
