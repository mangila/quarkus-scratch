CREATE OR REPLACE FUNCTION fn_customer_cache_evict() RETURNS trigger AS
$$
DECLARE
    v_customer_id uuid := NULL;
    v_channel     TEXT := 'customer_evict';
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_customer_id := OLD.id;
    ELSE
        v_customer_id := NEW.id;
    END IF;

    --! l2 cache evict
    DELETE
    FROM ttl_cache
    WHERE cache_key = v_customer_id::text;

    --! l1 cache evict
    PERFORM pg_notify(v_channel, v_customer_id::text);

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;