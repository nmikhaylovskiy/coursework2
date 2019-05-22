package sample;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class UiUtils {
    static double spacing = 5;

    static Spinner makeSpinner(int min, int max, int initial, Consumer<Integer> handler) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initial);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> handler.accept(newValue));
        return spinner;
    }

    static Button makeButton(String text, Action handler) {
        Button button = new Button(text);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handler.invoke());
        return button;
    }

    static Text makeBindedText(ObservableValue<? extends String> observable) {
        Text text = new Text();
        text.textProperty().bind(observable);
        return text;
    }

    static CheckBox makeCheckBox(String text, Consumer<Boolean> handler) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> handler.accept(newValue));
        return checkBox;
    }

    static VBox makeVBox(Node... children) {
        VBox vBox = new VBox(spacing, children);
        vBox.setPadding(new Insets(spacing));
        return vBox;
    }

    static HBox makeHBox(Node... children) {
        HBox hBox = new HBox(spacing, children);
        hBox.setAlignment(Pos.BASELINE_LEFT);
        return hBox;
    }
}

class RadioGroupBuilder {
    private final ToggleGroup group = new ToggleGroup();
    private final List<Node> items = new ArrayList<>();

    RadioGroupBuilder(String title) {
        items.add(new Text(title));
    }

    RadioGroupBuilder add(String text, Action handler) {
        return add(text, handler, false);
    }

    RadioGroupBuilder add(String text, Action handler, boolean selected) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setOnAction(e -> handler.invoke());
        radioButton.setSelected(selected);
        items.add(radioButton);
        return this;
    }

    HBox build() {
        HBox layout = new HBox(UiUtils.spacing);
        layout.getChildren().addAll(items);
        layout.setAlignment(Pos.BASELINE_LEFT);
        return layout;
    }
}
