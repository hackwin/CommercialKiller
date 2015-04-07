import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Commercial {

	Path path;
	byte[] data;
	
	Commercial(String path){
		this.path = Paths.get(path);
		try {
			data = Files.readAllBytes(this.path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}