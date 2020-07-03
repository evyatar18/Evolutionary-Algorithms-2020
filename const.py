import torch

length_params = 4
line_inputs = length_params * 30 + length_params * 2
line_length = line_inputs + 1

num_params = 30 * length_params
total_inputs = num_params + length_params * 2


device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

print(f"using device: {device}")