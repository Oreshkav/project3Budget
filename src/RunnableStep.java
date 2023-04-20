import java.io.IOException;
import java.text.ParseException;

public interface RunnableStep {
  void run() throws IOException, ParseException, InterruptedException;
}
