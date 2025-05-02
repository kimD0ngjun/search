import pandas as pd

ROUTE = "/Users/kimdongjun/desktop/xlsdata/"

df1 = pd.read_excel(ROUTE + "1_30000_20250419.xls")
df2 = pd.read_excel(ROUTE + "2_30000_20250419.xls")
df3 = pd.read_excel(ROUTE + "3_14953_20250419.xls")

df = pd.concat([df1, df2, df3], ignore_index=True)

columns_to_keep = ["표제어", "구분", "품사", "뜻풀이"]
df = df[columns_to_keep]

output_path = "/Users/kimdongjun/desktop/xlsdata/merged_dictionary.csv"
df.to_csv(output_path, index=False, encoding='utf-8-sig')

print(f"CSV 파일로 저장 완료: {output_path}")