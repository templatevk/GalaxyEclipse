use ge;

# dictionaries
insert into item_type
(item_type_name)
  values
  ('engine'),
  ('weapon'),
  ('bonus'),
  ('sale'),
  ('money');

insert into bonus_type
(bonus_type_name)
  values
  ('armor'),
  ('hp_regen'),
  ('energy_regen'),
  ('speed_rotation'),
  ('speed_move'),
  ('acceleration_rotation'),
  ('acceleration_move'),
  ('weapon_speed');

insert into weapon_type
(weapon_type_name)
  values
  ('rocket'),
  ('laser');

insert into location_object_behavior_type
(object_behavior_type_name)
  values
  ('ignored'),
  ('static'),
  ('drawable'),
  ('dynamic');

insert into location_object_type
(object_type_name)
  values
  ('star'),
  ('fog'),
  ('station'),
  ('asteroid'),
  ('rocket'),
  ('laser'),
  ('player');


# location
insert into location
(location_name, location_width, location_height)
  values
  (get_default_location_name(), 30000, 30000);


# item
insert into item
(item_name, item_description, item_price, item_type_id)
  values
# engine
  (get_default_engine_name(), 'engine escarpment 0', 50, get_item_type_id_by_name('engine')), -- escarpment (крутость)
  ('Engine "Torelin"', 'engine escarpment 1', 100, get_item_type_id_by_name('engine')), --
  ('Engine "Radamaes"', 'engine escarpment 2', 150, get_item_type_id_by_name('engine')), -- engine escarpment 2
  ('Engine "Hayale"', 'engine escarpment 3', 200, get_item_type_id_by_name('engine')), -- engine escarpment 3
# weapon laser
  (get_default_weapon_name(), 'weapon escarpment 0', 500, get_item_type_id_by_name('weapon')),
  ('Laser "Gyash"', 'weapon escarpment 1', 1000, get_item_type_id_by_name('weapon')),
# weapon rocket
  ('Rocket "Idario"', 'weapon escarpment 0', 2000, get_item_type_id_by_name('weapon')),
  ('Rocket "Pemelaya', 'weapon escarpment 1', 5000, get_item_type_id_by_name('weapon')),
# bonus armor
  ('Armor Module "Tortoise"', 'armor bonus', 600, get_item_type_id_by_name('bonus')),
  ('Armor Module "Armadillo"', 'armor bonus', 800, get_item_type_id_by_name('bonus')),
# bonus hp regen
  ('HP Regen Module "Lisard"', 'hp_regen bonus', 800, get_item_type_id_by_name('bonus')),
  ('HP Regen Module "Tarrudo"', 'hp_regen bonus', 1000, get_item_type_id_by_name('bonus')),
# bonus energy regen
  ('Energy Regen Module "Lanis"', 'energy bonus', 500, get_item_type_id_by_name('bonus')),
  ('Energy Regen Module "Tadpole"', 'energy bonus', 500, get_item_type_id_by_name('bonus')),
# sale
  ('Scarp Metal', 'scarp metal for sale', 100, get_item_type_id_by_name('sale')),
  ('Reactor Core', 'scarp metal for sale', 100, get_item_type_id_by_name('sale')),
  ('Trash', 'trash for sale', 5, get_item_type_id_by_name('sale')),
#money
  ('Gold', 'money doesn\'t bring happiness, but it helps', 1, get_item_type_id_by_name('money'));


insert into bonus
(bonus_value, item_id, bonus_type_id)
  values
# armor
  (1, get_item_id_by_name('Armor Module "Tortoise"'), get_bonus_type_id_by_name('armor')),
  (2, get_item_id_by_name('Armor Module "Armadillo"'), get_bonus_type_id_by_name('armor')),
# hp regen
  (5, get_item_id_by_name('HP Regen Module "Lisard"'), get_bonus_type_id_by_name('hp_regen')),
  (6, get_item_id_by_name('HP Regen Module "Tarrudo"'), get_bonus_type_id_by_name('hp_regen')),
# energy regen
  (3, get_item_id_by_name('Energy Regen Module "Lanis"'), get_bonus_type_id_by_name('energy_regen')),
  (3, get_item_id_by_name('Energy Regen Module "Tadpole"'), get_bonus_type_id_by_name('energy_regen'));

insert into weapon
(damage, delay_speed, bullet_speed, max_distance, energy_cost, weapon_type_id, item_id)
  values
# rocket
  (25, 10, 100, 200, 10, get_weapon_type_id_by_name('rocket'), get_item_id_by_name('Rocket "Idario"')),
  (50, 15, 85, 300, 15, get_weapon_type_id_by_name('rocket'), get_item_id_by_name('Rocket "Pemelaya')),
# laser
  (5, 1, 400, 100, 1, get_weapon_type_id_by_name('laser'), get_item_id_by_name('Laser "Flersiss"')),
  (10, 1, 350, 100, 2, get_weapon_type_id_by_name('laser'), get_item_id_by_name('Laser "Gyash"'));

insert into engine
(move_acceleration_bonus, move_max_speed_bonus, rotation_acceleration_bonus, rotation_max_speed_bonus, item_id)
  values
  (1, 5, 1, 2, get_item_id_by_name(get_default_engine_name())),
  (2, 6, 1, 2, get_item_id_by_name('Engine "Torelin"')),
  (3, 7, 2, 3, get_item_id_by_name('Engine "Radamaes"')),
  (4, 8, 3, 4, get_item_id_by_name('Engine "Hayale"'));


# ships
insert into ship_type
(ship_type_name, ship_type_armor, ship_type_armor_durability, ship_type_energy_max,
 ship_type_hp_max, ship_type_energy_regen, ship_type_hp_regen, ship_type_move_max_speed,
 ship_type_move_acceleration, ship_type_rotation_max_speed, ship_type_rotation_acceleration,
 weapon_slots_count, bonus_slots_count)
  values
  (get_default_ship_type_name(), 100, 100, 100, 500, 5, 10, 10, 0.2, 1, 0.025, 3, 3),
  ('Jass', 120, 120, 120, 600, 6, 12, 40, 4, 5, 1, 4, 4);


# test data
insert into player
(username, password, nickname, email, banned, activated)
  values
  ('test1', md5('test1'), 'test1', 'test1', 0, 1),
  ('test2', md5('test2'), 'test2', 'test2', 0, 1),
  ('test3', md5('test3'), 'test3', 'test3', 0, 1),
  ('test4', md5('test4'), 'test4', 'test4', 0, 1);

call activate_player(get_player_id_by_username('test1'));
call activate_player(get_player_id_by_username('test2'));
call activate_player(get_player_id_by_username('test3'));
call activate_player(get_player_id_by_username('test4'));