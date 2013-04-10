drop procedure if exists activate_player;

delimiter //

create procedure activate_player(player_id integer)
  begin

    declare ship_type_id int;
    declare location_object_id int;
    set ship_type_id = (select get_ship_type_id_by_name(get_default_ship_type_name()));

    update players p
    set is_activated = 1
    where p.player_id = player_id;

    insert into location_objects
    (location_object_behavior_type_id, location_object_type_id, object_native_id,
     location_id, position_x, position_y)
      values
      (get_location_object_behavior_type_id_by_name('static'),
       get_location_object_type_id_by_name('player_flight'),
       ship_type_id, get_location_id_by_name(get_default_location_name()),
       get_default_position_x(), get_default_position_y());

    set location_object_id = (select last_insert_id());

    insert into ship_configs
    (ship_config_move_max_speed, ship_config_rotation_max_speed, ship_config_move_acceleration,
     ship_config_rotation_acceleration, ship_config_armor, ship_config_energy_max,
     ship_config_hp_max, ship_config_energy_regen, ship_config_hp_regen,
     ship_type_id, engine_item_id, player_id)
      values
      ((select ship_type_move_max_speed
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_rotation_max_speed
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_move_acceleration
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_rotation_acceleration
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_armor
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_energy_max
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_hp_max
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_energy_regen
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_hp_regen
        from ship_types st
        where st.ship_type_id = ship_type_id),
       ship_type_id,
       get_item_id_by_name(get_default_engine_name()),
       player_id);

    insert into ship_states
    (ship_state_hp, ship_state_armor_durability, player_money,
     player_id, location_object_id)
      values
      ((select ship_type_hp_max
        from ship_types st
        where st.ship_type_id = ship_type_id),
       (select ship_type_armor_durability
        from ship_types st
        where st.ship_type_id = ship_type_id),
       get_default_player_money(), player_id,
       location_object_id);
  end
