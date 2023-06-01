#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFFER_SIZE 1024

void print_vector(int *arr, int n, int rank) {
  printf("P%d => ", rank);
  for (int i=0; i<n; i++) {
    printf("%d ", arr[i]);
  }
  printf("\n");
}

int get_array(const char* filename, int *array) {
    FILE* file = fopen(filename, "r");
    if (file == NULL) {
        printf("Error al abrir el archivo.\n");
        exit(1);
    }

    char line[BUFFER_SIZE];
    fgets(line, sizeof(line), file);
    fclose(file);

    char* token = strtok(line, ",");
    int i = 0;
    while (token != NULL && i < BUFFER_SIZE) {
        array[i] = atoi(token);
        token = strtok(NULL, ",");
        i++;
    }

    return i;
}

int max(int *array, int n) {
  int num = array[0];
  for (int i=1; i<n; i++) {
    if (num < array[i]) num = array[i];
  }
  return num;
}

int main(int argc, char** argv) {
    int rank, size;
    int n;
    int *sub_array;
    int result;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int* array = (int*)malloc(BUFFER_SIZE * sizeof(int));
    if (rank == 0) {
        // Proceso 0 obtiene el arreglo desde el archivo
        n = get_array("input.csv", array);
	if (n%4 != 0) {
	  printf("Error!!! El largo del vector debe ser multiplo de 4\n");
	  exit(1);
	}
    }


    // Envia tamaño del vector
    MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Crear espacio para la porción del arreglo que recibirá cada proceso
    int chunk_size = n / size;
    sub_array = (int*)malloc(chunk_size * sizeof(int));

    // Envío de la porción del arreglo desde el proceso 0 a los demás procesos
    MPI_Scatter(array, chunk_size, MPI_INT, sub_array, chunk_size, MPI_INT, 0, MPI_COMM_WORLD);


    // Imprime los procesos en orden
    MPI_Barrier(MPI_COMM_WORLD);
    for (int i=0; i<size; i++) {
      if (i == rank) {
	print_vector(sub_array, chunk_size, rank);
      }

      MPI_Barrier(MPI_COMM_WORLD);
    }


    // Encontrar el mayor
    int higher = max(sub_array, chunk_size);

    // Imprime los procesos en orden
    MPI_Barrier(MPI_COMM_WORLD);
    for (int i=0; i<size; i++) {
      if (i == rank) {
	printf("Mayor en P%d = %d\n", rank, higher);
      }

      MPI_Barrier(MPI_COMM_WORLD);
    }


    MPI_Reduce(&higher, &result, 1, MPI_INT, MPI_SUM, 0,MPI_COMM_WORLD);

    if (rank == 0) {
      printf("Suma total: %d\n", result);
    }


    free(sub_array);
    if (rank == 0) {
        free(array);
    }
    MPI_Finalize();

    return 0;
}
