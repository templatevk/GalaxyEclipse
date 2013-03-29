-----добавление юзера---------------
insert into players values ('Test', 'Test', 'Test', 2500)

--ship_state_move_speed, ship_state_rotation_speed, ship_state_hp, ship_state_armor_durability, ship_state_rotation_angle
insert into ship_states values (50, 10, 500, 100, 0)
insert into ship_states values (40, 8, 600, 120, 0)

--ship_type_name, ship_type_armor, ship_type_armor_durability, ship_type_energy_max, ship_type_hp_max, ship_type_energy_regen, ship_type_hp_regen,
--ship_type_move_max_speed, ship_type_move_acceleration, ship_type_rotation_max_speed, ship_type_rotation_acceleration, weapon_slots_count, bonus_slots_count
insert into ship_types values ('Ares', 100, 100, 100, 500, 5, 10, 50, 5, 10, 2, 3, 3)
insert into ship_types values ('Zeus', 120, 120, 120, 600, 6, 12, 40, 4, 9, 2, 4, 4) 

--ship_config_move_max_speed, ship_config_rotation_max_speed, ship_config_move_acceleration, ship_config_rotation_acceleration, ship_config_armor,
--ship_config_energy_max, ship_config_hp_max, ship_config_energy_regen, ship_config_hp_regen, ship_type_id, engine_item_id
insert into ship_configs values (50, 10, 5, 2, 100, 100, 500, 5, 10, 1, 1)
insert into ship_configs values (40, 8, 4, 1, 120, 120, 600, 4, 9, 2, 2)

--ship_config_id, item_id
insert into ship_config_bonus_slots values (1,1)
insert into ship_config_bonus_slots values (2,2)

--ship_config_id, item_id
insert into ship_config_weapon_slots values (1,1)
insert into ship_config_weapon_slots values (2,2)

-- engine, weapon, bonus, sale 
--name - field what need add
insert into item_types values ('engine')
insert into item_types values ('weapon')
insert into item_types values ('bonus')
insert into item_types values ('sale')

--item_name, item_description, item_price, item_type_id
insert into items values ('SSME', 'engine escarpment 0', 1,) --escarpment (крутость) 
insert into items values ('RS24', 'engine escarpment 1',100 , 2) --
insert into items values ('SE', 'engine escarpment 2',150,3) --engine escarpment 2
insert into items values ('LRE170','engine escarpment 3', 200, 4) --engine escarpment 3
insert into items values ('EL','weapon escarpment 0', 500,5) --
insert into items values ('UVGL','weapon escarpment 1', 1000,6) --
insert into items values ('GG','weapon escarpment 2', 2000,7) --
insert into items values ('GGWP', 'weapon escarpment 3', 5000,8) --
insert into items values ('speed bonus', ' speed bonus', 600,9) --
insert into items values ('HP bonus', 'HP bonus', 800,10) --bonus
insert into items values ('energy bonus', 'energy bonus', 500, 11) --bonus
insert into items values ('armor bonus', 'armor bonus', 1000,12) --bonus
insert into items values ('scarp metal', 'scarp metal for sale', 100,13) --металлолом 
insert into items values ('trash', 'trash for sale', 50, 14) --мусор 
insert into items values ('supplies', 'supplies for sale', 200, 15) --припасы 

--bonus_type_name
insert into bonus_types values ('armor')
insert into bonus_types values ('hp_regen')
insert into bonus_types values ('energy_regen')
insert into bonus_types values ('speed')
insert into bonus_types values ('acceleration')
insert into bonus_types values ('weapon')

--bonus_value, item_id change!!!!!
insert into bonuses values (5,5)
insert into bonuses values (5,5)
insert into bonuses values (5,5)
insert into bonuses values (5,5)
insert into bonuses values (5,5)
insert into bonuses values (5,5)

--weapon_type_name
insert into weapon_types values ('rocket')
insert into weapon_types values ('laser')

--change!!!!!!!!
--damage, delay_speed, bullet_speed, distance, energy_cost, weapon_type_id, item_id 
insert into weapons values (10, 25, 50, 250, 10, 1, 5)
insert into weapons values (50, 25, 50, 250, 10, 2, 6)

--change!!!!!!!!
--move_acceleration_bonus, move_max_speed_bonus, rotation_acceleration_bonus, rotation_max_speed_bonus, item_id
insert into engines values (15, 60, 15, 10, 1)
insert into engines values (10, 50, 10, 8, 2)

--change!!!!!!!!
--item_id, inventory_id
insert into inventories_items values (1,1)
insert into inventories_items values (2,1)
insert into inventories_items values (3,1)
insert into inventories_items values (4,1)
insert into inventories_items values (5,1)

--change!!!!!!!!
--location_name
insert into locations values ('Shine location')

--change!!!!!!!!
--location_static_objects_type_id,location_id, position_x, position_y
insert into location_static_objects values (1,1,1,11,1)

--change!!!!!!!!
--object_type_name
insert into location_static_object_types values ('station')

--change!!!!!!!!
--location_dynamic_objects_type_id, object_native_id, location_id, position_x, position_y
insert into location_dynamic_objects values (1,1,1,1,1)

--change!!!!!!!!
--object_type_name
insert into location_dynamic_object_types values ('player')
insert into location_dynamic_object_types values ('rocket')
insert into location_dynamic_object_types values ('bullet')
insert into location_dynamic_object_types values ('loot')