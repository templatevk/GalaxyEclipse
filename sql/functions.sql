delimiter //
create function get_item_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    declare item_type_id integer default 0;
    set item_type_id = (select
                          item_type_id
                        from item_types
                        where name = name);
    return item_type_id;
  end
//

create function get_ship_type_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    declare ship_type_id integer default 0;
    set ship_type_id = (select
                          ship_type_id
                        from ship_types
                        where ship_type_name = name);
    return ship_type_id;
  end
//

create function get_item_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    declare item_id integer default 0;
    set item_id = (select
                     item_id
                   from items
                   where item_name = name);
    return item_id;
  end
//

create function get_player_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    declare player_id integer default 0;
    set player_id = (select
                       player_id
                     from players
                     where nickname = nickname);
    return player_id;
  end
//


create function get_ship_config_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    declare ship_config_id integer default 0;
    set ship_config_id = (select
                            ship_config_id
                          from ship_configs
                            inner join players on ship_configs.player_id = players.player_id
                          where nickname = nickname);
    return ship_config_id;
  end
//

create function get_weapon_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    declare weapon_type_id integer default 0;
    set weapon_type_id = (select
                            weapon_type_id
                          from weapon_types
                          where weapon_type_name = name);
    return weapon_type_id;
  end
//

create function get_location_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    declare location_id integer default 0;
    set location_id = (select
                         location_id
                       from locations
                       where location_name = name);
    return location_id;
  end
//

create function get_location_static_object_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    declare location_static_object_type_id integer default 0;
    set location_static_object_type_id = (select
                                            location_static_object_type_id
                                          from location_static_object_types
                                          where object_type_name = name);
    return location_static_object_type_id;
  end
//

create function get_location_dynamic_object_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    declare location_dynamic_object_type_id integer default 0;
    set location_dynamic_object_type_id = (select
                                             location_dynamic_object_type_id
                                           from location_dynamic_object_types
                                           where object_type_name = name);
    return location_dynamic_object_type_id;
  end