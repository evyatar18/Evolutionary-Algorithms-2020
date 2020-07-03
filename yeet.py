import sys

def read_file(f):
    line = f.readline()

    while line:
        print(line.strip())
        line = f.readline()

def load_files(root, num_files):
    # xs = ["x" + str(i) for i in range(120)]
    # print(",".join(xs) + ",s")
    
    for file_index in range(num_files):
        file_path = root + "/" + str(file_index) + ".txt"

        try:
            with open(file_path) as f:
                read_file(f)
        except:
            print("an exception as occurred with: " + file_path)

load_files(sys.argv[1], int(sys.argv[2]))
