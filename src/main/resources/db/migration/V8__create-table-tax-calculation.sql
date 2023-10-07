CREATE TABLE IF NOT EXISTS tax_calculation (
  id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  tax_id UUID NOT NULL,
  user_id UUID NOT NULL,
  calculated_value DECIMAL(10, 2) NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES brtax_user(id),
  CONSTRAINT fk_tax FOREIGN KEY (tax_id) REFERENCES tax(id)
);
