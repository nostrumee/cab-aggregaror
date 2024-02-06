alter table passenger
    add column external_id uuid unique;

update passenger
set external_id = 'd6f3c9d1-de66-45ee-beb9-f371fa3a6378'
where email = 'johnsmith@example.com';

update passenger
set external_id = '3b7740b0-5dd7-4fce-88b9-66618931c0dd'
where email = 'janedoe@example.com';

update passenger
set external_id = '9e95dfb9-c037-428e-8e29-0131ea543ee3'
where email = 'michaeljohnson@example.com';

update passenger
set external_id = '858cbf8c-a88b-44a4-a9be-0d53b28893cd'
where email = 'emilybrown@example.com';

update passenger
set external_id = 'c23203aa-a199-4f73-ba4c-033d99cbe3b1'
where email = 'williammiller@example.com';

update passenger
set external_id = '6440c5ef-2dcc-4321-ae8f-43ce8323c9c6'
where email = 'sophiawilson@example.com';

update passenger
set external_id = '052b9615-e14d-4b54-94bd-2057a0c955a6'
where email = 'danieldavis@example.com';

update passenger
set external_id = 'b0dd1852-dc2b-472b-8982-700b87e409aa'
where email = 'oliviaanderson@example.com';

update passenger
set external_id = '5a4b620e-dc44-497b-a132-cb2cea3fba6b'
where email = 'roberttaylor@example.com';

update passenger
set external_id = 'a59a73d3-999c-4fe2-9a0a-9adfdc7a1af5'
where email = 'avathomas@example.com';

alter table passenger
    alter column external_id set not null;