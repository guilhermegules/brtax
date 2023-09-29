CREATE TABLE invoice (
  id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  invoiceNumber VARCHAR(9) NOT NULL,
  period DATE NOT NULL,
  private DECIMAL(10, 2) NOT NULL
);
