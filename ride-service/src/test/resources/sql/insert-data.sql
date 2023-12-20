insert into ride (id, passenger_id, driver_id, start_point, destination_point, status, created_date, accepted_date,
                  start_date, finish_date, estimated_cost)
values (1, 1, null, '123 Main St', '456 Elm St', 'CREATED', '2022-01-01 12:00:00', null, null, null, 6.3),
       (2, 1, null, '789 Oak St', '321 Pine St', 'REJECTED', '2022-01-02 13:00:00', null, null, null, 6.3),
       (3, 1, 1, '555 Maple St', '777 Cedar St', 'ACCEPTED', '2022-01-03 14:00:00', '2022-01-03 14:05:00',
        null, null, 6.3),
       (4, 1, 1, '999 Pine St', '222 Oak St', 'STARTED', '2022-01-04 15:00:00', '2022-01-04 15:05:00',
        '2022-01-05 15:10:00', null, 6.3),
       (5, 1, 1, '444 Elm St', '666 Main St', 'FINISHED', '2022-01-05 16:00:00', '2022-01-05 16:05:00',
        '2022-01-05 16:10:00', '2022-01-05 16:15:00', 16.3);