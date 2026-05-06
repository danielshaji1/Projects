import pandas as pd

company = {
    0: [186, 53, 'female', 79, 'black', 360901]
}

comp_df = pd.DataFrame(company).align(axis= -1, other=False)
print(comp_df)