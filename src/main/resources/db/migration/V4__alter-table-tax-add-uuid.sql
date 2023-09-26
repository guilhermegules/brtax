ALTER TABLE tax ALTER COLUMN id TYPE UUID USING gen_random_uuid();

ALTER TABLE tax ALTER COLUMN id SET DEFAULT gen_random_uuid();
