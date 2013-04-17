drop procedure if exists activate_player;

delimiter //

create procedure activate_player(player_id integer)
  begin

    declare ship_type_id       int;
    declare location_object_id int;
    declare ship_config_id     int;
    declare ship_state_id      int;

    set ship_type_id = (select get_ship_type_id_by_name(get_default_ship_type_name()));

    insert into location_object
    (location_object_behavior_type_id, location_object_type_id, object_native_id,
     location_id, position_x, position_y)
      values
      (get_location_object_behavior_type_id_by_name('ignored'),
       get_location_object_type_id_by_name('player_flight'),
       ship_type_id, get_location_id_by_name(get_default_location_name()),
       get_default_position_x(), get_default_position_y());
    set location_object_id = (select last_insert_id());

    insert into ship_config
    (ship_config_move_max_speed, ship_config_rotation_max_speed, ship_config_move_acceleration,
     ship_config_rotation_acceleration, ship_config_armor, ship_config_energy_max,
     ship_config_hp_max, ship_config_energy_regen, ship_config_hp_regen,
     ship_type_id, engine_item_id)
      values
      ((select ship_type_move_max_speed
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_rotation_max_speed
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_move_acceleration
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_rotation_acceleration
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_armor
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_energy_max
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_hp_max
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_energy_regen
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_hp_regen
        from ship_type st
        where st.ship_type_id = ship_type_id),
       ship_type_id,
       get_item_id_by_name(get_default_engine_name()));
    set ship_config_id = (select last_insert_id());

    insert into ship_state
    (ship_state_hp, ship_state_armor_durability)
      values
      ((select ship_type_hp_max
        from ship_type st
        where st.ship_type_id = ship_type_id),
       (select ship_type_armor_durability
        from ship_type st
        where st.ship_type_id = ship_type_id));
    set ship_state_id = (select last_insert_id());

    insert into inventory_item
    (amount, item_id, player_id)
      values
      (get_default_player_money(), get_item_id_by_name('Gold'), player_id);

    update player p
    set activated = 1, p.location_object_id = location_object_id,
      p.ship_config_id = ship_config_id, p.ship_state_id = ship_state_id
    where p.player_id = player_id;

  end
