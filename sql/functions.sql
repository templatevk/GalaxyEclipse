use ge;

delimiter //

# getter functions
create function get_item_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              item_type_id
            from item_type it
            where item_type_name = name);
  end
//

create function get_bonus_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              bonus_type_id
            from bonus_type
            where bonus_type_name = name);
  end
//
create function get_weapon_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              weapon_type_id
            from weapon_type
            where weapon_type_name = name);
  end
//

create function get_ship_type_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              ship_type_id
            from ship_type
            where ship_type_name = name);
  end
//

create function get_item_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              item_id
            from item
            where item_name = name);
  end
//

create function get_player_id_by_username(username varchar(16))
  returns integer deterministic
  begin
    return (select
              player_id
            from player p
            where p.username = username);
  end
//

create function get_player_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    return (select
              player_id
            from player p
            where p.nickname = nickname);
  end
//


create function get_ship_config_id_by_nickname(nickname varchar(16))
  returns integer deterministic
  begin
    return (select
              ship_config_id
            from ship_config
              inner join player p
                on ship_config.player_id = p.player_id
            where p.nickname = nickname);
  end
//


create function get_location_id_by_name(name varchar(32))
  returns integer deterministic
  begin
    return (select
              location_id
            from location
            where location_name = name);
  end
//

create function get_location_object_behavior_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              location_object_behavior_type_id
            from location_object_behavior_type
            where object_behavior_type_name = name);
  end
//

create function get_location_object_type_id_by_name(name varchar(16))
  returns integer deterministic
  begin
    return (select
              location_object_type_id
            from location_object_type
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
  returns float deterministic
  begin
    return 100;
  end
//

create function get_default_position_y()
  returns float deterministic
  begin
    return 100;
  end

# //
#
# create function get_default_()
#     returns deterministic
# begin
# return ;
# end