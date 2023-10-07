CREATE TABLE IF NOT EXISTS tax_calculation (
  id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL,
  calculated_value DECIMAL(10, 2) NOT NULL,
  tax_calculation_period DATE NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES brtax_user(id)
);
