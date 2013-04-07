delimiter //

# getter functions
create function get_item_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              item_type_id
            from item_types it
            where it.name = name);
  end
//

create function get_ship_type_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              ship_type_id
            from ship_types
            where ship_type_name = name);
  end
//

create function get_item_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              item_id
            from items
            where item_name = name);
  end
//

create function get_player_id_by_username(username varchar(16))
  returns integer deterministic
  begin
    return (select
              player_id
            from players p
            where p.username = username);
  end
//

create function get_player_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    return (select
              player_id
            from players p
            where p.nickname = nickname);
  end
//


create function get_ship_config_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    return (select
              ship_config_id
            from ship_configs
              inner join players p
                on ship_configs.player_id = p.player_id
            where p.nickname = nickname);
  end
//

create function get_bonus_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              bonus_type_id
            from bonus_types
            where bonus_type_name = name);
  end
//
create function get_weapon_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              weapon_type_id
            from weapon_types
            where weapon_type_name = name);
  end
//

create function get_location_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              location_id
            from locations
            where location_name = name);
  end
//

create function get_location_object_behavior_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              location_object_behavior_type_id
            from location_object_behavior_types
            where object_behavior_type_name = name);
  end
//

create function get_location_object_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              location_object_type_id
            from location_object_types
            where object_type_name = name);
  end
//

# default value functions
create function get_default_ship_type_name()
  returns varchar(32) deterministic
  begin
    return 'Ares';
  end
//

create function get_default_engine_name()
  returns varchar(32) deterministic
  begin
    return 'Engine "Salviran"';
  end
//

create function get_default_weapon_name()
  returns varchar(32) deterministic
  begin
    return 'Laser "Flersiss"';
  end
//

create function get_default_player_money()
  returns integer deterministic
  begin
    return 1000;
  end
//

create function get_default_location_name()
  returns varchar(32) deterministic
  begin
    return 'Sunshine location';
  end
//

create function get_default_position_x()
  returns integer deterministic
  begin
    return 2000;
  end
//

create function get_default_position_y()
  returns integer deterministic
  begin
    return 2000;
  end

# //
#
# create function get_default_()
#     returns deterministic
# begin
# return ;
# end