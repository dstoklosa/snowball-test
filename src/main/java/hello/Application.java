package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(final WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody final ArenaUpdate arenaUpdate) {
      System.out.println(arenaUpdate);
      final String[] commands = new String[]{"F", "R", "L", "T"};
      final String[] moveCommands = new String[]{"F", "F", "F", "F", "F", "F", "R", "L"};

        final int i = new Random().nextInt(8);

      if(isInDanger(arenaUpdate)) {
          return moveCommands[i];
      }

      return "T";
  }


  private List<Integer> getMyPostion(ArenaUpdate arenaUpdate) {
      PlayerState wasp = arenaUpdate.arena.state.get("Wasp");
      return Arrays.asList(wasp.x, wasp.y);
  }

  private boolean isInDanger(final ArenaUpdate aUpdate) {

      Map<String, PlayerState> positions = aUpdate.arena.state;
      List<Integer> myPostion = getMyPostion(aUpdate);

      if(myPostion == null) {
          return false;
      }
      
      Set<String> strings = positions.keySet();
      for (int i = 0; i < positions.size(); i++) {
          PlayerState playerState = positions.get(i);
          if (myPostion.get(0) == playerState.x
              && Math.abs(myPostion.get(1) - playerState.y) <= 3) {
                  System.out.println("In danger");
              return true;
          }
      }

      return false;
  }
}

