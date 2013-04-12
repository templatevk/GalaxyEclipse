-- 1 player
-- 1 ship_type
-- item_type, bonus_type, weapon_type (см. описание таблиц)
-- 1 engine, bonus of all types, weapon of all types ==> item of all types
-- 1 location
-- location_static_object_types, location_dynamic_object_types (см. описание таблиц)


create table player (
        player_id 		    integer auto_increment primary key,
        username 		    varchar(16) not null unique key,
        password 		    varchar(64) not null,
        nickname 		    varchar(16) not null unique key,
        player_money 		integer not null default 1000,
        ship_state_id 		integer unique key,
        ship_config_id 		integer unique key,
        player_inventory_id integer unique key
);

-- shipType
create table ship_state (		-- dynamic state of the shipType
        ship_state_id 			    integer auto_increment primary key,
        ship_state_move_speed 		integer not null,
        ship_state_rotation_speed 	integer not null,
        ship_state_hp 			    integer not null,
	    ship_state_armor_durability	integer not null,
        ship_state_rotation_angle 	integer not null
);
create table ship_type (
        ship_type_id 			        integer auto_increment primary key,
        ship_type_name 			        varchar(32) not null unique key,
        ship_type_armor 		        integer not null,	
        ship_type_armor_durability	    integer not null,	
	    ship_type_energy_max		    integer not null,
        ship_type_hp_max		        integer not null,
	    ship_type_energy_regen		    integer not null,
        ship_type_hp_regen		        integer not null,
        ship_type_move_max_speed 	    integer not null,
        ship_type_move_acceleration 	integer not null,
        ship_type_rotation_max_speed 	integer not null,
        ship_type_rotation_acceleration	integer not null,
        weapon_slots_count 		        integer not null,
        bonus_slots_count 		        integer not null
);
create table ship_config (		-- static state of the shipType
        ship_config_id 				        integer auto_increment primary key,
        ship_config_move_max_speed 		    integer not null,
        ship_config_rotation_max_speed 		integer not null,
        ship_config_move_acceleration	 	integer not null,
        ship_config_rotation_acceleration 	integer not null,
        ship_config_armor 			        integer not null,
	    ship_config_energy_max			    integer not null,
        ship_config_hp_max			        integer not null,
	    ship_config_energy_regen		    integer not null,
        ship_config_hp_regen			    integer not null,
        ship_type_id 				        integer not null,
        engine_item_id 				        integer not null
);
create table ship_config_bonus_slot (
        ship_config_bonus_slot_id 	    integer auto_increment primary key,
        ship_config_id 			        integer not null,
        item_id 			            integer not null
);

create table ship_config_weapon_slot (
        ship_config_weapon_slot_id 	integer auto_increment primary key,
        ship_config_id 			        integer not null,
        item_id 			            integer not null
);	


-- game item
create table item_type (
        item_type_id		integer auto_increment primary key,
        name varchar(16) 	not null unique key  -- engine, weapon, bonus, sale   
);

create table item (
        item_id 		    integer auto_increment primary key,
        item_name 		    varchar(32) not null unique key,
        item_description 	varchar(64) not null,
        item_price 		    integer not null,
        item_type_id 		integer not null
);

create table bonus_type (
        bonus_type_id		integer auto_increment primary key,
	    bonus_type_name		varchar(16) not null unique key    -- armor,  hp_regen, energy_regen, speed(rotation, move), acceleration(rotation,move), weapon
);

create table bonus (
        bonus_id 		integer auto_increment primary key,
        bonus_value 	integer not null,
        item_id 		integer not null
);
create table weapon_type (
        weapon_type_id	    integer auto_increment primary key,
	    weapon_type_name	varchar(16) not null unique key    -- rocket, laser 	
);
	
create table weapon (
        weapon_id 		    integer auto_increment primary key,
        damage 			    integer not null,
        delay_speed 		integer not null,
        bullet_speed 		integer not null,
        distance		    integer not null,
	    energy_cost		    integer not null,		-- amount of energy for one shot
        weapon_type_id		integer not null,
        item_id 		    integer not null
);

create table engine (
        engine_id 			        integer auto_increment primary key,
        move_acceleration_bonus 	integer not null,
        move_max_speed_bonus 		integer not null,
        rotation_acceleration_bonus integer not null,
        rotation_max_speed_bonus 	integer not null,
        item_id 			        integer not null
);


create table inventory_item (
        inventory_item_id 		integer auto_increment primary key,
        item_id 		    	integer not null,
        player_inventory_id 	integer not null
);

	
-- location
create table location (
        location_id         integer auto_increment primary key,
        location_name       varchar(32) not null
);
create table location_static_objects (
        location_static_object_id       integer auto_increment primary key,
        location_static_object_type_id integer not null,
        location_id                     integer not null,
        position_x                      integer not null,
        position_y                      integer not null
);

create table location_static_object_types (
        location_static_object_type_id       integer auto_increment primary key,
        object_type_name                     varchar(16) not null -- sation, start, ...
);

create table location_dynamic_objects (
        location_dynamic_object_id          integer auto_increment primary key,
        location_dynamic_object_type_id    integer not null,       
        object_native_id                    integer not null,   -- defines the primary key of table depending on object type
        location_id                         integer not null,
        position_x                          integer not null,
        position_y                          integer not null
);

create table location_dynamic_object_types (
        location_dynamic_object_type_id       integer auto_increment primary key,
        object_type_name                      varchar(16) not null -- bullet, rocket, player, loot
);

alter table location_static_objects
        add constraint fk_location_static_object_location foreign key (location_id) references location(location_id),
        add constraint fk_location_static_object_location_static_object_type foreign key (location_static_object_type_id) references location_static_object_types(location_static_object_type_id);

alter table location_dynamic_objects
        add constraint fk_location_dynamic_object_location foreign key (location_id) references location(location_id),
        add constraint fk_location_dynamic_object_location_dynamic_object_type foreign key (location_dynamic_object_type_id) references location_dynamic_object_types(location_dynamic_object_type_id);        
        
alter table bonus
        add constraint fk_bonus_type_item foreign key (item_id) references item (item_id);

alter table engine
        add constraint fk_engine_type_item foreign key (item_id) references item (item_id);

alter table inventory_item
        add constraint fk_inventory_item_item foreign key (item_id) references item (item_id),
        add constraint fk_inventory_item_player foreign key (player_inventory_id) references player (player_inventory_id);

alter table item
        add constraint fk_item_item_type foreign key (item_type_id) references item_type (item_type_id);

alter table player
        add constraint fk_player_inventory foreign key (player_inventory_id) references inventory_item (player_inventory_id),
        add constraint fk_player_ship_config foreign key (ship_config_id) references ship_config (ship_config_id),
        add constraint fk_player_ship_state foreign key (ship_state_id) references ship_state (ship_state_id);

alter table ship_config
        add constraint fk_ship_config_engine_item foreign key (engine_item_id) references item (item_id),
        add constraint fk_ship_config_ship_type foreign key (ship_type_id) references ship_type (ship_type_id);

alter table ship_config_bonus_slot
        add constraint fk_ship_config_bonus_slot_item foreign key (item_id) references item (item_id),
        add constraint fk_ship_config_bonus_slot_ship_config foreign key (ship_config_id) references ship_config (ship_config_id);

alter table ship_config_weapon_slot
        add constraint fk_ship_config_weapon_slot_item foreign key (item_id) references item (item_id),
        add constraint fk_ship_config_weapon_slot_ship_config foreign key (ship_config_id) references ship_config (ship_config_id);

alter table weapon
        add constraint fk_weapon_type_item foreign key (item_id) references item (item_id);

