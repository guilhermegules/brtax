ALTER TABLE brtax_user ALTER COLUMN id TYPE UUID USING gen_random_uuid();

ALTER TABLE brtax_user ALTER COLUMN id SET DEFAULT gen_random_uuid();
