'use client';

import { useEffect, useState } from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
import { BookOpen, GraduationCap, Award, AlertCircle } from 'lucide-react';

// Registramos los componentes del gráfico
ChartJS.register(ArcElement, Tooltip, Legend);

// Definimos qué forma tienen los datos que vienen de Java
interface Stats {
  averageNote: number;
  creditsObtained: number;
  progressPercentage: number;
  totalSubjects: number;
}

export default function Home() {
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Esta función llama a tu Backend
    fetch('http://localhost:8080/api/subjects/stats')
      .then((res) => res.json())
      .then((data) => {
        setStats(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error conectando con Java:', error);
        setLoading(false);
      });
  }, []);

  // Configuración del Gráfico
  const dataChart = {
    labels: ['Superado', 'Pendiente'],
    datasets: [
      {
        data: stats ? [stats.creditsObtained, 240 - stats.creditsObtained] : [0, 100],
        backgroundColor: ['#3b82f6', '#e5e7eb'], // Azul y Gris
        borderWidth: 0,
      },
    ],
  };

  if (loading) return <div className="p-10 text-white">Cargando datos de la UNED...</div>;
  if (!stats) return <div className="p-10 text-red-400">Error: No se pudo conectar con el Backend. ¿Está encendido?</div>;

  return (
    <main className="min-h-screen bg-gray-900 text-white p-8 md:p-12">
      
      {/* CABECERA */}
      <header className="mb-12 flex items-center gap-3">
        <GraduationCap size={40} className="text-blue-500" />
        <div>
          <h1 className="text-3xl font-bold">Mi Progreso UNED</h1>
          <p className="text-gray-400">Grado en Ingeniería Informática</p>
        </div>
      </header>

      {/* TARJETAS DE RESUMEN */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        
        {/* Tarjeta 1: Créditos */}
        <div className="bg-gray-800 p-6 rounded-2xl border border-gray-700 flex items-center gap-4">
          <div className="bg-blue-900/50 p-3 rounded-full">
            <BookOpen className="text-blue-400" size={24} />
          </div>
          <div>
            <p className="text-gray-400 text-sm">Créditos Superados</p>
            <p className="text-2xl font-bold">{stats.creditsObtained} <span className="text-gray-500 text-lg">/ 240</span></p>
          </div>
        </div>

        {/* Tarjeta 2: Nota Media */}
        <div className="bg-gray-800 p-6 rounded-2xl border border-gray-700 flex items-center gap-4">
          <div className="bg-green-900/50 p-3 rounded-full">
            <Award className="text-green-400" size={24} />
          </div>
          <div>
            <p className="text-gray-400 text-sm">Nota Media Ponderada</p>
            <p className="text-2xl font-bold">{stats.averageNote === 0 ? '-' : stats.averageNote}</p>
          </div>
        </div>

        {/* Tarjeta 3: Asignaturas */}
        <div className="bg-gray-800 p-6 rounded-2xl border border-gray-700 flex items-center gap-4">
          <div className="bg-purple-900/50 p-3 rounded-full">
            <AlertCircle className="text-purple-400" size={24} />
          </div>
          <div>
            <p className="text-gray-400 text-sm">Asignaturas Totales</p>
            <p className="text-2xl font-bold">{stats.totalSubjects}</p>
          </div>
        </div>
      </div>

      {/* GRÁFICO DE PROGRESO */}
      <section className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-gray-800 p-8 rounded-2xl border border-gray-700 flex flex-col items-center">
          <h2 className="text-xl font-semibold mb-6">Progreso de la Carrera</h2>
          <div className="w-64 h-64 relative">
            <Doughnut data={dataChart} />
            <div className="absolute inset-0 flex items-center justify-center flex-col pointer-events-none">
              <span className="text-3xl font-bold">{stats.progressPercentage}%</span>
              <span className="text-xs text-gray-400">Completado</span>
            </div>
          </div>
        </div>

        <div className="bg-gray-800 p-8 rounded-2xl border border-gray-700">
          <h2 className="text-xl font-semibold mb-4">Próximos Pasos</h2>
          <p className="text-gray-400">
            Tienes <strong>{stats.totalSubjects}</strong> asignaturas registradas en el sistema.
            <br /><br />
            Para llegar al 50% de la carrera, necesitas aprobar aproximadamente <strong>{120 - (stats?.creditsObtained || 0)}</strong> créditos más.
            ¡Sigue así!
          </p>
        </div>
      </section>

    </main>
  );
}