package com.subway2feira.controllers.components;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.subway2feira.models.Station;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {

    private ComboBox<Station> comboBoxFBox;
    private StringBuilder sb;
    private ObservableList<Station> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteComboBoxListener(final ComboBox<Station> comboBoxFBox, final ComboBox<Station> comboBoxSBox,
            List<Station> sList) {
        this.comboBoxFBox = comboBoxFBox;

        sb = new StringBuilder();
        data = FXCollections.observableArrayList(sList);

        this.comboBoxFBox.setEditable(true);
        this.comboBoxFBox.setOnKeyPressed(eventHandler());
        this.comboBoxFBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
        this.comboBoxFBox.setConverter(stringConvert(sList));
        this.comboBoxFBox.valueProperty().addListener(comboxSelect(comboBoxSBox));
    }

    private ChangeListener<Station> comboxSelect(ComboBox<Station> combox) {
        return new ChangeListener<Station>() {
            @Override
            public void changed(ObservableValue ov, Station oldValue, Station newValue) {

                if (newValue == null || newValue.getId() == null)
                    return;

                List<Station> f = data.stream().filter(predicate -> !predicate.equals(newValue)).collect(toList());

                if (!combox.getItems().equals(f))
                    combox.setItems(FXCollections.observableArrayList(f));
            }
        };
    }

    private StringConverter<Station> stringConvert(final List<Station> sList) {
        return new StringConverter<Station>() {

            @Override
            public String toString(Station station) {
                return station != null ? station.getName() : "";
            }

            @Override
            public Station fromString(final String string) {

                if (string.isEmpty() || string == null)
                    return new Station();
                return sList.stream().filter(predicate -> predicate.getName().contains(string)).findFirst().get();

            }

        };
    }

    private EventHandler<KeyEvent> eventHandler() {
        return new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBoxFBox.hide();
            }
        };
    }

    @Override
    public void handle(KeyEvent event) {

        if (AutoCompleteComboBoxListener.this.comboBoxFBox.getEditor().getText().isEmpty())
            return;

        switch (event.getCode()) {
            case UP:
                keyUP();
                break;
            case DOWN:
                keyDOWN();
                break;
            case BACK_SPACE:
            case DELETE:
                keySpaceDelete();
                break;
            case RIGHT:
            case LEFT:
            case HOME:
            case END:
            case TAB:
            case ENTER:
                break;
            default:
                completeCombox();
                break;
        }
    }

    private void completeCombox() {
        ObservableList<Station> list = FXCollections.observableArrayList();

        for (Station station : this.comboBoxFBox.getItems()) {
            if (station.getName().toLowerCase().startsWith(
                    this.comboBoxFBox
                            .getEditor().getText().toLowerCase())) {
                list.add(station);
            }
        }

        String t = comboBoxFBox.getEditor().getText();

        comboBoxFBox.setItems(list);
        comboBoxFBox.getEditor().setText(t);
        if (!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if (!list.isEmpty()) {
            comboBoxFBox.show();
        }
    }

    private void keySpaceDelete() {
        moveCaretToPos = true;
        caretPos = comboBoxFBox.getEditor().getCaretPosition();
    }

    private void keyDOWN() {
        if (!comboBoxFBox.isShowing()) {
            comboBoxFBox.show();
        }
        keyUP();
    }

    private void keyUP() {
        caretPos = -1;
        moveCaret(comboBoxFBox.getEditor().getText().length());
    }

    private void moveCaret(int textLength) {
        if (caretPos == -1) {
            comboBoxFBox.getEditor().positionCaret(textLength);
        } else {
            comboBoxFBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }

}