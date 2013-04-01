

create table players (
  player_id    integer auto_increment primary key,
  username     varchar(16) not null unique key,
  password     varchar(64) not null,
  nickname     varchar(16) not null unique key,
  player_money integer     not null default 1000
);

-- ship
create table ship_states (-- dynamic state of the ship
  ship_state_id               integer auto_increment primary key,
  ship_state_move_speed       integer not null,
  ship_state_rotation_speed   integer not null,
  ship_state_hp               integer not null,
  ship_state_armor_durability integer not null,
  ship_state_rotation_angle   integer not null,
  player_id                   integer not null                          -- ADDED
);

create table ship_types (
  ship_type_id                    integer auto_increment primary key,
  ship_type_name                  varchar(32) not null unique key,
  ship_type_armor                 integer     not null,
  ship_type_armor_durability      integer     not null,
  ship_type_energy_max            integer     not null,
  ship_type_hp_max                integer     not null,
  ship_type_energy_regen          integer     not null,
  ship_type_hp_regen              integer     not null,
  ship_type_move_max_speed        integer     not null,
  ship_type_move_acceleration     integer     not null,
  ship_type_rotation_max_speed    integer     not null,
  ship_type_rotation_acceleration integer     not null,
  weapon_slots_count              integer     not null,
  bonus_slots_count               integer     not null
);
create table ship_configs (-- static state of the ship
  ship_config_id                    integer auto_increment primary key,
  ship_config_move_max_speed        integer not null,
  ship_config_rotation_max_speed    integer not null,
  ship_config_move_acceleration     integer not null,
  ship_config_rotation_acceleration integer not null,
  ship_config_armor                 integer not null,
  ship_config_energy_max            integer not null,
  ship_config_hp_max                integer not null,
  ship_config_energy_regen          integer not null,
  ship_config_hp_regen              integer not null,
  ship_type_id                      integer not null,
  engine_item_id                    integer not null,
  player_id                         integer not null                          -- ADDED
);
create table ship_config_bonus_slots (
  ship_config_bonus_slots_id integer auto_increment primary key,
  ship_config_id             integer not null,
  item_id                    integer not null
);

create table ship_config_weapon_slots (
  ship_config_weapon_slots_id integer auto_increment primary key,
  ship_config_id              integer not null,
  item_id                     integer not null
);


-- game items
create table item_types (
  item_type_id integer auto_increment primary key,
  name         varchar(16) not null unique key  -- engine, weapon, bonus, sale
);

create table items (
  item_id          integer auto_increment primary key,
  item_name        varchar(32) not null unique key,
  item_description varchar(64) not null,
  item_price       integer     not null,
  item_type_id     integer     not null
);

create table bonus_types (
  bonus_type_id   integer auto_increment primary key,
  bonus_type_name varchar(16) not null unique key    -- armor,  hp_regen, energy_regen, speed(rotation, move), acceleration(rotation,move), weapon
);

create table bonuses (
  bonus_id    integer auto_increment primary key,
  bonus_value integer not null,
  item_id     integer not null
);
create table weapon_types (
  weapon_type_id   integer auto_increment primary key,
  weapon_type_name varchar(16) not null unique key    -- rocket, laser
);

create table weapons (
  weapon_id      integer auto_increment primary key,
  damage         integer not null,
  delay_speed    integer not null,
  bullet_speed   integer not null,
  distance       integer not null,
  energy_cost    integer not null, -- amount of energy for one shot
  weapon_type_id integer not null,
  item_id        integer not null
);

create table engines (
  engine_id                   integer auto_increment primary key,
  move_acceleration_bonus     integer not null,
  move_max_speed_bonus        integer not null,
  rotation_acceleration_bonus integer not null,
  rotation_max_speed_bonus    integer not null,
  item_id                     integer not null
);


create table inventories_items (
  inventory_item_id integer auto_increment primary key,
  item_id           integer not null,
  player_id         integer not null                          -- ADDED
);


-- locations
create table locations (
  location_id   integer auto_increment primary key,
  location_name varchar(32) not null
);
create table location_static_objects (
  location_static_object_id      integer auto_increment primary key,
  location_static_object_type_id integer not null,
  location_id                    integer not null,
  position_x                     integer not null,
  position_y                     integer not null
);

create table location_static_object_types (
  location_static_object_type_id integer auto_increment primary key,
  object_type_name               varchar(16) not null -- sation, start, ...
);

create table location_dynamic_objects (
  location_dynamic_object_id      integer auto_increment primary key,
  location_dynamic_object_type_id integer not null,
  object_native_id                integer not null, -- defines the primary key of table depending on object type
  location_id                     integer not null,
  position_x                      integer not null,
  position_y                      integer not null
);

create table location_dynamic_object_types (
  location_dynamic_object_type_id integer auto_increment primary key,
  object_type_name                varchar(16) not null -- bullet, rocket, player, loot
);

alter table location_static_objects
add constraint fk_location_static_object_location foreign key (location_id) references locations (location_id),
add constraint fk_location_static_object_location_static_object_type foreign key (location_static_object_type_id) references location_static_object_types (location_static_object_type_id);

alter table location_dynamic_objects
add constraint fk_location_dynamic_object_location foreign key (location_id) references locations (location_id),
add constraint fk_location_dynamic_object_location_dynamic_object_type foreign key (location_dynamic_object_type_id) references location_dynamic_object_types (location_dynamic_object_type_id);

alter table bonuses
add constraint fk_bonus_type_item foreign key (item_id) references items (item_id);

alter table engines
add constraint fk_engine_type_item foreign key (item_id) references items (item_id);

alter table inventories_items
add constraint fk_inventories_items_item foreign key (item_id) references items (item_id),
add constraint fk_inventories_items_player foreign key (player_id) references players (player_id);

alter table items
add constraint fk_item_item_type foreign key (item_type_id) references item_types (item_type_id);

alter table ship_states
add constraint fk_ship_state_player foreign key (player_id) references players (player_id);

alter table ship_configs
add constraint fk_ship_config_engine_item foreign key (engine_item_id) references items (item_id),
add constraint fk_ship_config_ship_type foreign key (ship_type_id) references ship_types (ship_type_id),
add constraint fk_ship_config_player foreign key (player_id) references players (player_id);

alter table ship_config_bonus_slots
add constraint fk_ship_config_bonus_slots_item foreign key (item_id) references items (item_id),
add constraint fk_ship_config_bonus_slots_ship_config foreign key (ship_config_id) references ship_configs (ship_config_id);

alter table ship_config_weapon_slots
add constraint fk_ship_config_weapon_slots_item foreign key (item_id) references items (item_id),
add constraint fk_ship_config_weapon_slots_ship_config foreign key (ship_config_id) references ship_configs (ship_config_id);

alter table weapons
add constraint fk_weapon_type_item foreign key (item_id) references items (item_id);

