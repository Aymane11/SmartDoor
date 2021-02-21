package smartdoor.models;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import smartdoor.utils.FileSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Session {

    private int id;
    private Timestamp date_in;
    private String filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_in() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date_in.getTime()));
    }

    public Timestamp getDate_inTimestamp() {
        return this.date_in;
    }

    public void setDate_in() {
        this.date_in = new Timestamp(new Date().getTime());
    }

    public void setDate_in(Timestamp date_in) {
        this.date_in = date_in;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename() {
        this.filename = UUID.randomUUID().toString() + ".jpg";
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Button getFilenameLink() {
        Button btn = new Button("See image");
        btn.getStyleClass().add("btn");

        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                Stage stage = new Stage();
                  InputStream stream = new FileInputStream(FileSystem.getDataResource(filename));
                  Image image = new Image(stream);
                  //Creating the image view
                  ImageView imageView = new ImageView();
                  //Setting image to the image view
                  imageView.setImage(image);
                  //Setting the image view parameters
                  imageView.setX(10);
                  imageView.setY(10);
                  imageView.setFitWidth(image.getWidth());
                  imageView.setPreserveRatio(true);
                  //Setting the Scene object
                  Group root = new Group(imageView);
                  Scene scene = new Scene(root, image.getWidth() + 10, image.getHeight() + 10);
                  stage.setTitle("Image Preview");
                  stage.setScene(scene);
                  stage.show();
            } catch(Exception e) {
                  e.printStackTrace();
            }
        });

        return btn;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", date_in='" + date_in + '\'' +
                ", file='" + filename + '\'' +
                '}';
    }
}
