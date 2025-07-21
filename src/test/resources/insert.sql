insert into protocol_azur(protocol_number, measurement_purpose, documents_number, result_visual_inspection, climate_data)
values ('0113-3', 'приёмо-сдаточные испытания', 'РД 06-572-03', 'удовлетворительные', '+18');

insert into protocol_azur(protocol_number, measurement_purpose, documents_number, result_visual_inspection, climate_data)
values ('0113-4', 'приёмо-сдаточные испытания', 'ГД 06-572-03', 'удовлетворительные', '+20');

insert into azur_test(protocol_azur_id, test_type_id, parameters, norm, result, conclusion, notation) VALUES (100000, 1, 'парам 1', 4.3, 4.3, 'заключение', 'примечание');
insert into azur_test(protocol_azur_id, test_type_id, parameters, norm, result, conclusion, notation) VALUES (100000, 2, 'парам 2', 5.4, 5.4, 'заключение2', 'примечание2');
insert into azur_test(protocol_azur_id, test_type_id, parameters, norm, result, conclusion, notation) VALUES (100000, 3, 'парам 3', 2.9, 2.9, 'заключение3', 'примечание3');
insert into azur_test(protocol_azur_id, test_type_id, parameters, norm, result, conclusion, notation) VALUES (100001, 1, 'парам 11',5.9, 5.9, 'заключение11', 'примечание11');