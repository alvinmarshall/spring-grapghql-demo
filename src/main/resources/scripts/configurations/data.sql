--countries
insert into countries(country_iso3_code,country_iso2_code,name,phone_code,country_id)
values ('GHA','GH','Ghana','233',11);

insert into countries(country_iso3_code,country_iso2_code,name,phone_code,country_id)
values ('NGA','NG','Nigeria','234',7);

--states
insert into states(id,name,code,country_id)
values ('343','Greater Accra Region','Greater Accra Region','GHA');

insert into states(id,name,code,country_id)
values ('338','Ashanti Region','Ashanti Region','GHA');

insert into states(id,name,code,country_id)
values ('69','Cross River','Cross River','NGA');

insert into states(id,name,code,country_id)
values ('84','Lagos','Lagos','NGA');
