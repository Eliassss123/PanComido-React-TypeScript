export const SERVICE_URLS = {
  users: "https://36txx18f-8081.brs.devtunnels.ms",
  inventory: "https://36txx18f-8082.brs.devtunnels.ms",
  orders: "https://36txx18f-8084.brs.devtunnels.ms",
  gateway: "https://36txx18f-8085.brs.devtunnels.ms",
  reports: "https://36txx18f-8086.brs.devtunnels.ms",
  eureka: "https://36txx18f-8761.brs.devtunnels.ms",
  config: "https://36txx18f-8888.brs.devtunnels.ms",
} as const;

export type ServiceName = keyof typeof SERVICE_URLS;

export const getServiceUrl = (service: ServiceName) => SERVICE_URLS[service];

