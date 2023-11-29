import javafx.scene.control.TableCell;
import javafx.scene.control.CheckBox;

public class CheckBoxCell extends TableCell<Item, Boolean> {
    private final CheckBox checkBox;

    public CheckBoxCell() {
        checkBox = new CheckBox();
        checkBox.setOnAction(e -> {
            Item item = getTableView().getItems().get(getIndex());
            item.setSelected(checkBox.isSelected());
        });
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            setGraphic(checkBox);
            checkBox.setSelected(item);
        }
    }
}
