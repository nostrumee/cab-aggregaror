alter table driver
    add column external_id uuid unique;

update driver
set external_id = 'd3849c45-a4f6-4e2a-8289-6b662076fabf'
where email = 'johndoe@example.com';

update driver
set external_id = '02a7aaaa-0e7e-4596-adf5-a8e00fbce29f'
where email = 'janesmith@example.com';

update driver
set external_id = '166e87cb-8a3e-4861-bac3-9acca2a4e589'
where email = 'michaeljohnson@example.com';

update driver
set external_id = '09f716ca-d1c3-4787-bdba-f81399f7c34c'
where email = 'emilywilliams@example.com';

update driver
set external_id = 'cb3a9180-cecc-4a3d-862b-b70c52678834'
where email = 'davidbrown@example.com';

update driver
set external_id = '39320680-3c63-4f0a-a98b-0e15e6f43a3c'
where email = 'sarahdavis@example.com';

update driver
set external_id = 'adde3766-4108-48c8-bf8b-6779494c0580'
where email = 'matthewmiller@example.com';

update driver
set external_id = '18e1b417-86df-4a52-b925-f4f3267eb840'
where email = 'oliviataylor@example.com';

update driver
set external_id = '1e2b8721-a67e-462a-9359-1d50dff6ceb2'
where email = 'jamesanderson@example.com';

update driver
set external_id = 'd41cc987-cd8b-48ee-bd31-bede42d174a9'
where email = 'emmamoore@example.com';

alter table driver
    alter column external_id set not null;