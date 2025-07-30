package ru.shred.electromachine.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

@Route("azur")
public class AzurUi extends VerticalLayout {

    private final ProtocolAzurService protocolAzurService;

    @Value("${electromachine.ip.address}")
    private String ipAddress;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private ProtocolAzur protocolAzur;

    private Grid<ProtocolAzur> protocolAzurGrid = new Grid<>(ProtocolAzur.class);

    private TextField protocolNumberTextField = new TextField("Номер протокола");
    private TextField measurementPurposeTextField = new TextField("Цель измерений");
    private TextField documentsNumberTextField = new TextField("Номер документов");
    private TextField resultVisualInspectionTextField = new TextField("Результаты визуального осмотра");
    private TextField climateDataTextField = new TextField("Климатические данные");

    // Диалоговое окно для редактирования/добавления протоколов Азур
    private Dialog azurDialog = new Dialog();

    private ConfirmDialog confirmDeleteProtocolDialog = new ConfirmDialog("Удалить протокол",
            "Вы уверены, что хотите удалить протокол?", "Да", event -> deleteAzur(protocolAzur.getId()),
            "Отмена", this::closeConfirmDeleteProtocolDialog);

    @Autowired
    public AzurUi(ProtocolAzurService protocolAzurService) {
        this.protocolAzurService = protocolAzurService;

        protocolAzurGrid.setColumns();

        protocolAzurGrid.addColumn(ProtocolAzur::getProtocolNumber).setHeader("Протокол");
        protocolAzurGrid.addColumn(ProtocolAzur::getMeasurementPurpose).setHeader("Цель испытаний");
        protocolAzurGrid.addColumn(ProtocolAzur::getDocumentsNumber).setHeader("Документы");
        protocolAzurGrid.addColumn(ProtocolAzur::getResultVisualInspection).setHeader("Результат визуального осмотра");
        protocolAzurGrid.addColumn(ProtocolAzur::getClimateData).setHeader("Климатические данные");
        protocolAzurGrid.addColumn(
                new NativeButtonRenderer<>("Рез-ты испытаний",
                        clickedItem -> {
                            UI.getCurrent().getPage().setLocation(
                                    String.format("%s:%s%s/azur/test?protocol-id=%s", ipAddress, port, contextPath, clickedItem.getId()));
                        })
        );
        protocolAzurGrid.addColumn(
                new NativeButtonRenderer<>("Изменить",
                        clickedItem -> {
                            protocolAzur = protocolAzurService.getById(clickedItem.getId());
                            azurDialog.removeAll();
                            azurDialog.add(buildAzurResultLayout(protocolAzur));
                            azurDialog.open();
                        })
        );
        protocolAzurGrid.addColumn(
                new NativeButtonRenderer<>("Удалить",
                        clickedItem -> {
                            protocolAzur = protocolAzurService.getById(clickedItem.getId());
                            confirmDeleteProtocolDialog.open();
                        })
        );

        protocolAzurGrid.getColumns().forEach(column -> column.setSortable(true));
        protocolAzurGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        protocolAzurGrid.setItems(protocolAzurService.getAll());

        add(protocolAzurGrid, buildButtonLayout());
        setAlignItems(Alignment.CENTER);
    }

    @NonNull
    private HorizontalLayout buildButtonLayout() {
        Button addResultButton = new Button("Добавить", VaadinIcon.PLUS.create());
        addResultButton.getStyle().set("border-radius", "12px").set("background-color", "green").set("color", "white");
        addResultButton.addClickListener(event -> {
            azurDialog.removeAll();
            azurDialog.add(buildAzurResultLayout(new ProtocolAzur()));
            azurDialog.open();
        });

        Button refreshButton = new Button("Обновить", VaadinIcon.REFRESH.create());
        refreshButton.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");
        refreshButton.addClickListener(event -> protocolAzurGrid.setItems(protocolAzurService.getAll()));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(addResultButton, refreshButton);

        return buttonLayout;
    }

    private void deleteAzur(Long id) {
        protocolAzurService.delete(id);
        protocolAzurGrid.setItems(protocolAzurService.getAll());
    }

    @NonNull
    private VerticalLayout buildAzurResultLayout(ProtocolAzur protocolAzur) {
        protocolNumberTextField.setWidth("300px");
        measurementPurposeTextField.setWidth("300px");
        documentsNumberTextField.setWidth("300px");
        resultVisualInspectionTextField.setWidth("300px");
        climateDataTextField.setWidth("300px");

        Button saveButton = new Button("Сохранить", VaadinIcon.CHECK.create());
        saveButton.getStyle().set("border-radius", "12px").set("background-color", "blue").set("color", "white");
        saveButton.addClickListener(event -> saveProtocolAzur(protocolAzur));

        VerticalLayout protocolAzurLayout = new VerticalLayout();
        protocolAzurLayout.add(
                protocolNumberTextField, measurementPurposeTextField, documentsNumberTextField,
                resultVisualInspectionTextField, climateDataTextField, saveButton);

        if (protocolAzur.getId() != null) {
            setValuesProtocolAzurLayout(protocolAzur);
        } else {
            setValuesProtocolAzurLayoutOnDefault();
        }

        return protocolAzurLayout;
    }


    private void setValuesProtocolAzurLayout(ProtocolAzur protocolAzur) {
        protocolNumberTextField.setValue(protocolAzur.getProtocolNumber());
        measurementPurposeTextField.setValue(protocolAzur.getMeasurementPurpose());
        documentsNumberTextField.setValue(protocolAzur.getDocumentsNumber());
        resultVisualInspectionTextField.setValue(protocolAzur.getResultVisualInspection());
        climateDataTextField.setValue(protocolAzur.getClimateData());
    }

    private void setValuesProtocolAzurLayoutOnDefault() {
        protocolNumberTextField.setValue("");
        measurementPurposeTextField.setValue("");
        documentsNumberTextField.setValue("");
        resultVisualInspectionTextField.setValue("");
        climateDataTextField.setValue("");
    }

    private void saveProtocolAzur(ProtocolAzur protocolAzur) {
        Long id = protocolAzur.getId();

        if (id == null) {
            addProtocolAzur();
        } else {
            updateProtocolAzurTest(id);
        }
    }

    private void addProtocolAzur() {
        protocolAzurService.save(buildProtocolAzurFromForm());
        closeDialog();
    }

    private void updateProtocolAzurTest(Long id) {
        ProtocolAzur forUpdate = buildProtocolAzurFromForm();
        forUpdate.setId(id);

        protocolAzurService.update(forUpdate);
        closeDialog();
    }

    private void closeDialog() {
        azurDialog.close();
        Notification.show("Изменения сохранены");
        protocolAzurGrid.setItems(protocolAzurService.getAll());
    }

    private ProtocolAzur buildProtocolAzurFromForm() {
        return ProtocolAzur.builder()
                .protocolNumber(protocolNumberTextField.getValue())
                .measurementPurpose(measurementPurposeTextField.getValue())
                .documentsNumber(documentsNumberTextField.getValue())
                .resultVisualInspection(resultVisualInspectionTextField.getValue())
                .climateData(climateDataTextField.getValue())
                .build();
    }

    private void closeConfirmDeleteProtocolDialog(ConfirmDialog.CancelEvent cancelEvent) {
        confirmDeleteProtocolDialog.close();
    }
}
