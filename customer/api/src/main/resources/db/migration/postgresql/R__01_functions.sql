CREATE OR REPLACE FUNCTION fn_cache_evict() RETURNS trigger AS
$$
DECLARE
    var_id      UUID := NEW.id;
    var_channel TEXT := 'cache_evict';
BEGIN
    IF TG_OP = 'DELETE' THEN
        var_id := OLD.id;
    END IF;

    --! l2 cache evict
    DELETE
    FROM ttl_cache
    WHERE cache_key = var_id::text;

    --! l1 cache evict
    PERFORM pg_notify(var_channel, json_build_object('tg_table_name', TG_TABLE_NAME, 'id', var_id)::text);

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;