package ru.shred.electromachine.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

@Route("azur")
public class AzurUi extends VerticalLayout {

    private final ProtocolAzurService protocolAzurService;

    private Grid<ProtocolAzur> protocolAzurGrid = new Grid<>(ProtocolAzur.class);

    @Autowired
    public AzurUi(ProtocolAzurService protocolAzurService) {
        this.protocolAzurService = protocolAzurService;

        protocolAzurGrid.setColumns();

        protocolAzurGrid.addColumn(ProtocolAzur::getProtocolNumber).setHeader("Протокол");
        protocolAzurGrid.addColumn(ProtocolAzur::getMeasurementPurpose).setHeader("Цель испытаний");
        protocolAzurGrid.addColumn(ProtocolAzur::getDocumentsNumber).setHeader("Документы");
        protocolAzurGrid.addColumn(ProtocolAzur::getResultVisualInspection).setHeader("Результат визуального осмотра");
        protocolAzurGrid.addColumn(ProtocolAzur::getClimateData).setHeader("Климатические данные");

        protocolAzurGrid.getColumns().forEach(column -> column.setSortable(true));
        protocolAzurGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        protocolAzurGrid.setItems(protocolAzurService.getAll());

        add(protocolAzurGrid);
    }
}
