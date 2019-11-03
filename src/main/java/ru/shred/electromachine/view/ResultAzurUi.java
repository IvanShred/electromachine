package ru.shred.electromachine.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import ru.shred.electromachine.model.AzurTestResult;
import ru.shred.electromachine.model.TestTypeAzur;
import ru.shred.electromachine.service.AzurTestResultService;
import ru.shred.electromachine.service.ProtocolAzurService;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.shred.electromachine.model.TestTypeAzur.RATED_VOLTAGE;

@Route("azur/test")
public class ResultAzurUi extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    private AzurTestResultService azurTestResultService;

    @Autowired
    private ProtocolAzurService protocolAzurService;

    private AzurTestResult azurTestResult;

    private Long protocolId;

    private Grid<AzurTestResult> azurTestResultGrid = new Grid<>(AzurTestResult.class);

    private ComboBox<String> comboBoxTestType = new ComboBox<>("Тип испытания:");
    private TextArea parametersTextArea = new TextArea("Параметры");
    private NumberField normNumberField = new NumberField("Норма");
    private NumberField resultNumberField = new NumberField("Результат");
    private TextArea conclusionTextArea = new TextArea("Заключение");
    private TextArea notationTextArea = new TextArea("Примечание");

    // Диалоговое окно для редактирования/добавления результатов испытаний Азур
    private Dialog azurResultDialog = new Dialog();

    private ConfirmDialog confirmDeleteAllTestResultsDialog = new ConfirmDialog("Удалить результаты испытаний",
            "Вы уверены, что хотите удалить все результаты испатаний?", "Да", this::deleteAll,
            "Отмена", this::closeConfirmDeleteAllDialog);

    private ConfirmDialog confirmDeleteTestResultDialog = new ConfirmDialog("Удалить результат испытаний",
            "Вы уверены, что хотите удалить результат испатаний?", "Да", event -> deleteAzurTestResult(azurTestResult.getId()),
            "Отмена", this::closeConfirmDeleteTestResultDialog);

    public ResultAzurUi() {
    }

    public ResultAzurUi(AzurTestResultService azurTestResultService, ProtocolAzurService protocolAzurService, Long protocolId) {
        this.azurTestResultService = azurTestResultService;
        this.protocolAzurService = protocolAzurService;
        this.protocolId = protocolId;

        // Текстовое поле для отображения номера протокола
        TextField protocolNumber = new TextField("Протокол");
        protocolNumber.setValue(String.valueOf(protocolAzurService.getById(protocolId).getProtocolNumber()));
        protocolNumber.setWidth("7vw");
        protocolNumber.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        protocolNumber.setReadOnly(true);
        add(protocolNumber);

        azurResultDialog.setWidth("350px");

        azurTestResultGrid.setColumns();

        azurTestResultGrid.addColumn(AzurTestResult::getTestType).setHeader("Тип испытания");
        azurTestResultGrid.addColumn(AzurTestResult::getParameters).setHeader("Параметры испытания");
        azurTestResultGrid.addColumn(new NumberRenderer<>(AzurTestResult::getNorm, NumberFormat.getInstance())).setHeader("Норма");
        azurTestResultGrid.addColumn(new NumberRenderer<>(AzurTestResult::getResult, NumberFormat.getInstance())).setHeader("Результаты");
        azurTestResultGrid.addColumn(AzurTestResult::getConclusion).setHeader("Заключение");
        azurTestResultGrid.addColumn(AzurTestResult::getNotation).setHeader("Примечание");
        azurTestResultGrid.addColumn(
                new NativeButtonRenderer<>("Изменить",
                        clickedItem -> {
                            azurTestResult = azurTestResultService.getById(clickedItem.getId());
                            azurResultDialog.removeAll();
                            azurResultDialog.add(buildAzurResultLayout(azurTestResult));
                            azurResultDialog.open();
                        })
        );
        azurTestResultGrid.addColumn(
                new NativeButtonRenderer<>("Удалить",
                        clickedItem -> {
                            azurTestResult = azurTestResultService.getById(clickedItem.getId());
                            confirmDeleteTestResultDialog.open();
                        })
        );

        azurTestResultGrid.getColumns().forEach(column -> column.setSortable(true));
        azurTestResultGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        azurTestResultGrid.setItems(azurTestResultService.getAllByProtocolId(protocolId));

        add(azurTestResultGrid, buildButtonLayout());
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String protocolId) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        UI.getCurrent().add(new ResultAzurUi(azurTestResultService, protocolAzurService, Long.valueOf(parametersMap.get("protocol-id").get(0))));
    }

    @NonNull
    private HorizontalLayout buildButtonLayout() {
        Button addResultButton = new Button("Добавить", VaadinIcon.PLUS.create());
        addResultButton.getStyle().set("border-radius", "12px").set("background-color", "green").set("color", "white");
        addResultButton.addClickListener(event -> {
            azurResultDialog.removeAll();
            azurResultDialog.add(buildAzurResultLayout(new AzurTestResult()));
            azurResultDialog.open();
        });

        Button refreshButton = new Button("Обновить", VaadinIcon.REFRESH.create());
        refreshButton.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");
        refreshButton.addClickListener(event -> azurTestResultGrid.setItems(azurTestResultService.getAllByProtocolId(protocolId)));

        Button deleteAllButton = new Button("Удалить все", VaadinIcon.REFRESH.create());
        deleteAllButton.getStyle().set("border-radius", "12px").set("background-color", "red").set("color", "white");
        deleteAllButton.addClickListener(event -> confirmDeleteAllTestResultsDialog.open());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(addResultButton, refreshButton, deleteAllButton);

        return buttonLayout;
    }

    @NonNull
    private VerticalLayout buildAzurResultLayout(AzurTestResult azurTestResult) {
        comboBoxTestType.setItems(Arrays.stream(TestTypeAzur.values())
                .map(TestTypeAzur::getName)
                .collect(Collectors.toList()));
        comboBoxTestType.setValue(RATED_VOLTAGE.getName());

        parametersTextArea.setWidth("300px");
        normNumberField.setValue(0d);
        resultNumberField.setValue(0d);
        conclusionTextArea.setWidth("300px");
        notationTextArea.setWidth("300px");

        Button saveButton = new Button("Сохранить", VaadinIcon.CHECK.create());
        saveButton.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");
        saveButton.addClickListener(event -> saveAzurTestResult(azurTestResult));

        VerticalLayout azurResultLayout = new VerticalLayout();
        azurResultLayout.add(
                comboBoxTestType, parametersTextArea, normNumberField, resultNumberField, conclusionTextArea, notationTextArea, saveButton);

        if (azurTestResult.getId() != null) {
            setValuesAzurResultLayout(azurTestResult);
        } else {
            setValuesAzurResultLayoutOnDefault();
        }

        return azurResultLayout;
    }

    private void setValuesAzurResultLayout(AzurTestResult azurTestResult) {
        comboBoxTestType.setValue(azurTestResult.getTestType());
        parametersTextArea.setValue(azurTestResult.getParameters());
        normNumberField.setValue(azurTestResult.getNorm());
        resultNumberField.setValue(azurTestResult.getResult());
        conclusionTextArea.setValue(azurTestResult.getConclusion());
        notationTextArea.setValue(azurTestResult.getNotation());
    }

    private void setValuesAzurResultLayoutOnDefault() {
        parametersTextArea.setValue("");
        normNumberField.setValue(0D);
        resultNumberField.setValue(0D);
        conclusionTextArea.setValue("");
        notationTextArea.setValue("");
    }

    private void saveAzurTestResult(AzurTestResult azurTestResult) {
        Long id = azurTestResult.getId();

        if (id == null) {
            addAzurTestResult();
        } else {
            updateAzurTestResult(id);
        }
    }

    private void addAzurTestResult() {
        azurTestResultService.save(buildAzurTestResultFromForm());
        closeDialog();
    }

    private void updateAzurTestResult(Long id) {
        AzurTestResult forUpdate = buildAzurTestResultFromForm();
        forUpdate.setId(id);

        azurTestResultService.update(forUpdate);
        closeDialog();
    }

    private void closeDialog() {
        azurResultDialog.close();
        Notification.show("Изменения сохранены");
        azurTestResultGrid.setItems(azurTestResultService.getAllByProtocolId(protocolId));
    }

    private AzurTestResult buildAzurTestResultFromForm() {
        return AzurTestResult.builder()
                .protocolAzurId(protocolId)
                .testType(comboBoxTestType.getValue())
                .parameters(parametersTextArea.getValue())
                .norm(normNumberField.getValue())
                .result(resultNumberField.getValue())
                .conclusion(conclusionTextArea.getValue())
                .notation(notationTextArea.getValue())
                .build();
    }

    private void deleteAzurTestResult(Long id) {
        azurTestResultService.delete(id);
        azurTestResultGrid.setItems(azurTestResultService.getAllByProtocolId(protocolId));
    }

    private void deleteAll(ConfirmDialog.ConfirmEvent event) {
        azurTestResultService.deleteAllByProtocolId(protocolId);
        azurTestResultGrid.setItems(azurTestResultService.getAllByProtocolId(protocolId));
    }

    private void closeConfirmDeleteAllDialog(ConfirmDialog.CancelEvent cancelEvent) {
        confirmDeleteAllTestResultsDialog.close();
    }

    private void closeConfirmDeleteTestResultDialog(ConfirmDialog.CancelEvent cancelEvent) {
        confirmDeleteTestResultDialog.close();
    }
}
