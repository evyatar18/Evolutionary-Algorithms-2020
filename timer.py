

class Timer:
    from time import time as current_time

    def __init__(self):
        self.started = False
        self.start_time = 0
        self.stop_time = 0

    def reset(self):
        self.__init__()

    def restart(self):
        self.reset()
        self.start()

    def start(self):
        self.start_time = self.current_time()
        self.started = True

    def stop(self):
        self.stop_time = self.current_time()
        self.started = False

    def time_passed(self):
        if self.started:
            return self.current_time() - self.start_time

        else:
            return self.stop_time - self.start_time

    def __str__(self):
        return f"{self.time_passed()}s"

    def print(self, start_msg = ""):
        print(start_msg, self)
