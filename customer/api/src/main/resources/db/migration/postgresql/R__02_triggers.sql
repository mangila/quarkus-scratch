DROP TRIGGER IF EXISTS trg_customer_cache_evict ON customer;

CREATE TRIGGER trg_customer_cache_evict
    AFTER UPDATE OR DELETE
    ON customer
    FOR EACH ROW
EXECUTE FUNCTION fn_customer_cache_evict();