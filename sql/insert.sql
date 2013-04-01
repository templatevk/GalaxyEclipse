insert into players
('username', 'password', 'nickname', 'gold')
  values
  ('Test1', 'Test1', 'Nick1', 2500),
  ('Test2', 'Test2', 'Nick2', 2500);


insert into ship_states
('ship_state_move_speed', 'ship_state_rotation_speed', 'ship_state_hp', 'ship_state_armor_durability', 'ship_state_rotation_angle', 'player_id',
 'is_banned', 'is_activated')
  values
  (0, 0, 0, 0, 0, get_player_id_by_nickname('Nick1'), 0, 1),
  (0, 0, 0, 0, 0, get_player_id_by_nickname('Nick2'), 0, 1);

insert into ship_types
('ship_type_name', 'ship_type_armor', 'ship_type_armor_durability', 'ship_type_energy_max', 'ship_type_hp_max', 'ship_type_energy_regen',
 'ship_type_hp_regen', 'ship_type_move_max_speed', 'ship_type_move_acceleration', 'ship_type_rotation_max_speed', 'ship_type_rotation_acceleration',
 'weapon_slots_count', 'bonus_slots_count')
  values
  ('Ares', 100, 100, 100, 500, 5, 10, 50, 5, 10, 2, 3, 3),
  ('Jass', 120, 120, 120, 600, 6, 12, 40, 4, 9, 2, 4, 4);


insert into item_types
('name')
  values
  ('engine'),
  ('weapon'),
  ('bonus'),
  ('sale');


--
 insert into items
('item_name', 'item_description', 'item_price', 'item_type_id')
  values
  ('Engine "Salviran"', 'engine escarpment 0', 50, get_item_type_id_by_name('engine')), - - escarpment (крутость)
('Engine "Torelin"', 'engine escarpment 1', 100, get_item_type_id_by_name('engine')), --
    ('Engine "Radamaes"', 'engine escarpment 2', 150, get_item_type_id_by_name('engine')), - - engine escarpment 2
('Engine "Hayale"', 'engine escarpment 3', 200, get_item_type_id_by_name('engine')), - - engine escarpment 3
('Laser "Flersiss"', 'weapon escarpment 0', 500, get_item_type_id_by_name('weapon')), --
    ('Laser "Gyash"', 'weapon escarpment 1', 1000, get_item_type_id_by_name('weapon')), --
    ('Rocket "Idario"', 'weapon escarpment 0', 2000, get_item_type_id_by_name('weapon')), --
    ('Rocket "Pemelaya', 'weapon escarpment 1', 5000, get_item_type_id_by_name('weapon')), --
    ('Armor Module "Tortoise"', ' armor bonus', 600, get_item_type_id_by_name('bonus')), --
    ('Armor Module "Armadillo"', ' armor bonus', 800, get_item_type_id_by_name('bonus')), --
    ('HP Regen Module "Lisard"', 'hp_regen bonus', 800, get_item_type_id_by_name('bonus')), - - bonus
('HP Regen Module "Tarrudo"', 'hp_regen bonus', 1000, get_item_type_id_by_name('bonus')), - - bonus
('Energy Regen Module "Lanis"', 'energy bonus', 500, get_item_type_id_by_name('bonus')), - - bonus
('Energy Regen Module "Tadpole"', 'energy bonus', 500, get_item_type_id_by_name('bonus')), - - bonus
('Scarp Metal', 'scarp metal for sale', 100, get_item_type_id_by_name('sale')), - - детали на продажу
('Reactor Core', 'scarp metal for sale', 100, get_item_type_id_by_name('sale')), - - детали  на продажу
('Trash', 'trash for sale', 5, get_item_type_id_by_name('sale'));
-- мусор на продажу


insert into ship_configs
('ship_config_move_max_speed', 'ship_config_rotation_max_speed', 'ship_config_move_acceleration', 'ship_config_rotation_acceleration',
'ship_config_armor', 'ship_config_energy_max', 'ship_config_hp_max', 'ship_config_energy_regen', 'ship_config_hp_regen', 'ship_type_id',
'engine_item_id', 'player_id')
values
(50, 10, 5, 2, 104, 100, 500, 5, 10, get_ship_type_id_by_name('Ares'), get_item_id_by_name('Engine "Salviran"'), get_player_id_by_nickname('Nick1')),
(40, 8, 4, 1, 120, 120, 600, 4, 9, get_ship_type_id_by_name('Jass'), get_item_id_by_name('Engine "Torelin"'), get_player_id_by_nickname('Nick2'));


insert into inventories_items
('amount', 'item_id', 'player_id')
  values
  (1, get_item_id_by_name('HP Regen Module "Tarrudo"'), get_player_id_by_nickname('Nick1')),
  (1, get_item_id_by_name('Energy Regen Module "Lanis"'), get_player_id_by_nickname('Nick1')),
  (1, get_item_id_by_name('Another Things'), get_player_id_by_nickname('Nick1')),
  (1, get_item_id_by_name('Engine "Radamaes"'), get_player_id_by_nickname('Nick2')),
  (1, get_item_id_by_name('Armor Module "Armadillo"'), get_player_id_by_nickname('Nick2'));

insert into ship_config_bonus_slots
('ship_config_id', 'item_id')
  values
  (get_ship_config_id_by_nickname('Nick1'), get_item_id_by_name('Armor Module "Armadillo"'));


insert into ship_config_weapon_slots
('ship_config_id', 'item_id')
  values
  (get_ship_config_id_by_nickname('Nick1'), get_item_id_by_name('Laser "Flersiss"')),
  (get_ship_config_id_by_nickname('Nick1'), get_item_id_by_name('Laser "Flersiss"')),
  (get_ship_config_id_by_nickname('Nick2'), get_item_id_by_name('Rocket "Pemelaya"'));

--
 insert into bonus_types
('bonus_type_name')
  values
  ('armor'),
  ('hp_regen'),
  ('energy_regen'),
  ('speed'),
  ('acceleration'),
  ('weapon');


insert into bonuses
('bonus_value', 'item_id')
  values
  (1, get_item_id_by_name('Armor Module "Tortoise"')),
  (2, get_item_id_by_name('Armor Module "Armadillo"')),
  (5, get_item_id_by_name('HP Regen Module "Lisard"')),
  (6, get_item_id_by_name('HP Regen Module "Tarrudo"')),
  (3, get_item_id_by_name('Energy Regen Module "Lanis"')),
  (3, get_item_id_by_name('Energy Regen Module "Tadpole"'));


insert into weapon_types
('weapon_type_name')
  values
  ('rocket'),
  ('laser');


insert into weapons
('damage', 'delay_speed', 'bullet_speed', 'distance', 'energy_cost', 'weapon_type_id', 'item_id')
  values
  (25, 10, 100, 200, 10, get_weapon_type_id_by_name('rocket'), get_item_id_by_name('Rocket "Idario"')),
  (50, 15, 85, 300, 15, get_weapon_type_id_by_name('rocket'), get_item_id_by_name('Rocket "Pemelaya')),
  (5, 1, 400, 100, 1, get_weapon_type_id_by_name('laser'), get_item_id_by_name('Laser "Flersiss"')),
  (10, 1, 350, 100, 2, get_weapon_type_id_by_name('laser'), get_item_id_by_name('Laser "Gyash"'));


insert into engines
('move_acceleration_bonus', 'move_max_speed_bonus', 'rotation_acceleration_bonus', 'rotation_max_speed_bonus', 'item_id')
  values
  (1, 5, 1, 2, get_item_id_by_name('Engine "Salviran"')),
  (2, 6, 1, 2, get_item_id_by_name('Engine "Torelin"')),
  (3, 7, 2, 3, get_item_id_by_name('Engine "Radamaes"')),
  (4, 8, 3, 4, get_item_id_by_name('Engine "Hayale"'));


insert into locations
('location_name', 'location_width', 'location_height')
  values
  ('Sunshine location', 4000, 4000);


insert into location_static_object_types
('object_type_name')
  values
  ('station');


insert into location_static_objects
('location_static_object_type_id', 'location_id', 'position_x', 'position_y')
  values
  (get_location_static_object_type_id_by_name('station'), get_location_id_by_name('Sunshine location'), 500, 500);


insert into location_dynamic_object_types
('object_type_name')
  values
  ('player'),
  ('rocket'),
  ('bullet'),
  ('loot');

insert into location_dynamic_objects
('location_dynamic_objects_type_id', 'object_native_id', 'location_id', 'position_x', 'position_y')
  values
  (get_location_dynamic_object_type_id_by_name('player'), get_player_id_by_nickname('Nick1'),
   get_location_id_by_name('Sunshine location'), 700, 700);