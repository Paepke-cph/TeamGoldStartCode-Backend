package webscraper;

public class TagDTO {

  public TagDTO(TagCounter tc) {
    this.url = tc.getUrl();
    this.title = tc.getTitle();
    this.divCount = tc.getDivCount();
    this.bodyCount = tc.getBodyCount();
  }
  
  public String url;
  public String title;
  public int divCount;
  public int bodyCount;
}
